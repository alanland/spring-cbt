package ttx.service

import com.gemstone.org.json.JSONArray
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.EmptyResultDataAccessException
import ttx.service.base.BaseService
import ttx.service.base.CreationService
import ttx.util.config.AppProfile
import ttx.util.json.JsonCalc

/**
 * ＠author 王成义 
 * @created 2015-01-19.
 */
// todo 所有方法使用缓存
@Configuration
class PermissionService extends BaseService {

    @Bean
    PermissionService permissionService() {
        new PermissionService()
    }

    @Autowired
    CreationService creationService

    def getDefaultNav(String db) {
        creationService.getSysNavigator(db)
    }

    // 获取菜单用于系统显示，只返回有权限的菜单
    def getSecurableNav(String db, String role) {
        List dbNav = creationService.getNavigator(db, role)
        List defaultNav = creationService.getSysNavigator(db)

        List res = []
        defaultNav.each {
            def checked = checked(it, defaultNav, dbNav)
            if (checked) { // 有权限才添加
                it.checked = checked
                res.add it
            }
        }
        res
    }

    // 获取菜单用于系统显示，只返回有权限的菜单
    def getSecurableUserNav(String db, String user) {
        // 获取单据 todo 菜单顺序有问题，要改成遍历admin菜单
        List defaultNav = creationService.getSysNavigator(db)
        List roles = template(db).queryForList(
                "select role_code from ${AppProfile.TABLE_USER_ROLE} where user_code=?",
                String.class,
                user
        )
        if (user == 'admin' || roles.contains('admin')) {
            return defaultNav
        }
        List res = []
        roles.each { String role ->
            List roleNav = getSecurableNav(db, role)
            roleNav.each {
                String navItem
                if (!res.find({ it.id == navItem.id })) {
                    res.add(navItem)
                }
            }
        }
        res
    }

    // 获取所有菜单，并修正 checked 属性
    def getEditableNav(String db, String role) {
        List dbNav = creationService.getNavigator(db, role)
        List defaultNav = creationService.getSysNavigator(db)

        List res = []
        defaultNav.each {
            def checked = checked(it, defaultNav, dbNav)
            if (checked != null) {
                it.checked = checked
                res.add it
            }
        }
        res
    }

    def createOrUpdateNav(String db, String role, List data) {
        creationService.updateNavigator(db, role, data)
    }

    // 返回 list json
    private def getDefaultActions(String db, String tid, String oid) { // todo cache
        def viewData = [:]
        try {
            viewData = new JsonSlurper().parseText(template(db).queryForMap(
                    "select structure from ${AppProfile.TABLE_WSO_DATA} where tid=? and oid=?",
                    tid, oid
            ).structure)
        } catch (EmptyResultDataAccessException e) {
        }

        JsonCalc.getActions(viewData)
    }

    // 返回list
    private def getDbActions(String db, String role, String tid, String oid, String nid) {
        try {
            return new JsonSlurper().parseText(template(db).queryForMap(
                    "select structure from ${AppProfile.TABLE_ACTION_RIGHT} where tid=? and oid=? and nid=? and role=?",
                    tid, oid, nid, role
            ).structure)
        } catch (EmptyResultDataAccessException e) {
            return []
        }
    }

    // todo 是否有必要存在
    def getSecurableActions(String db, String role, String tid, String oid, String nid) {
        List defaultActions = getDefaultActions(db, tid, oid)
        List dbActions = getDbActions(db, role, tid, oid, nid)
        List res = []
        defaultActions.each {
            def checked = checked(it, defaultActions, dbActions)
            if (checked) { // 有权限才添加
                it.checked = checked
                res.add it
            }
        }
    }

    def getEditableActions(String db, String role, String tid, String oid, String nid) {
        List defaultActions = getDefaultActions(db, tid, oid)
        List dbActions = getDbActions(db, role, tid, oid, nid)
        defaultActions.each {
            def checked = checked(it, defaultActions, dbActions)
//            if (checked != null) {
            it.checked = checked
//            }
        }
        defaultActions
    }

    def createOrUpdateActions(String db, String role, String tid, String oid, String nid, List list) { // todo
        int count = template(db).queryForObject(
                "select count(1) from ${AppProfile.TABLE_ACTION_RIGHT} where role=? and tid=? and oid=? and nid=?",
                Integer.class, role, tid, oid, nid
        )
        if (count == 0) {
            insert(db).withTableName(AppProfile.TABLE_ACTION_RIGHT).execute([
                    role     : role,
                    tid      : tid,
                    oid      : oid,
                    nid      : nid,
                    structure: (list as JSONArray).toString()
            ])
        } else {
            update(db).withTableName(AppProfile.TABLE_ACTION_RIGHT).execute([
                    structure: (list as JSONArray).toString()
            ], [
                    tid: tid,
                    oid: oid,
                    nid: nid
            ])
        }
        [code: 0, desc: 'ok']
    }

    def removeActions(String db, String role) {}

    boolean hasChildren(Object id, List list) {
        list.find {
            it.id = id
        } == null
    }

    boolean exist(Map item, List list) {
        def find = list.find {
            it.id == item.id
        }
        find
    }

    boolean parentChecked(Map item, List list) { // 父亲是否checked
        def parent = list.find {
            it.id = item.parent
        }
        parent && parent.checked == true
    }

    // null: 表示删除的东西
    // mixin
    // true
    // false
    Object checked(Map item, List defaultNav, List dbNav) {
        if (!exist(item, defaultNav)) { // 删除的一些东西
            return null
        }
        if (!exist(item, dbNav)) { // 新增的item
            return parentChecked(item, dbNav)
        }
        // 原来就存在的，两边都存在
        dbNav.find({ it.id == item.id }).checked
    }
}
