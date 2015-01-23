package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*
import ttx.service.base.CreationService
import ttx.util.config.AppProfile

import javax.servlet.http.HttpServletRequest

/**
 * Created by journey on 14-12-5.
 */
@Configuration
@RestController
@RequestMapping('/rest/creation')
class CreationController extends BaseController {
    @Autowired
    CreationService service

//    // 获取数据库所有表
    @RequestMapping(value = 'navigator', method = RequestMethod.GET)
    def navigator(HttpServletRequest request) {
        service.getSysNavigator(getDb(request))
    }
//
//    @RequestMapping(value = 'navigator/{key}', method = RequestMethod.GET)
//    def navigatorGet(@PathVariable String key) {
//        service.getNavigator(key)
//    }
//
    // 更新表模型
    @RequestMapping(value = 'navigator', method = RequestMethod.PUT, consumes = 'application/json')
    def updateNavigator(HttpServletRequest request, @RequestBody Map map) {
        service.updateNavigator(getDb(request), 'admin', map.data)
    }

//    // 更新表模型 todo 权限
//    @RequestMapping(value = 'navigator/{key}', method = RequestMethod.PUT, consumes = 'application/json')
//    def updateNavigator(HttpServletRequest request, @PathVariable String key, @RequestBody Map map) {
//        service.updateNavigator(getDb(request), key, map.data)
//    }

    // 获取数据库所有表
    @RequestMapping('tables')
    def tables(HttpServletRequest request) {
        def list = []
        service.getTables(getDb(request)).each {
            list.add(id: it, name: it)
        }
        list
    }

    // 获取所有字段
    @RequestMapping('tables/{table}/fields')
    def fields(HttpServletRequest request, @PathVariable("table") String table) {
        service.getFields(getDb(request), table)
    }

    /*
     * 表模型
     */

    // 获取所有表模型定义
    @RequestMapping(value = 'tableModels', method = RequestMethod.GET)
    def tableModels(HttpServletRequest request) {
        service.getModels(getDb(request), AppProfile.TABLE_TABLE_MODEL)
    }

    // 新建表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.POST, consumes = 'application/json')
    def createTableModel(HttpServletRequest request, @RequestBody Map map) {
        service.createModel(getDb(request), AppProfile.TABLE_TABLE_MODEL, map)
    }
    // 更新表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateTableModel(HttpServletRequest request, @RequestBody Map map) {
        service.updateModel(getDb(request), AppProfile.TABLE_TABLE_MODEL, map)
    }

    // 删除表模型
    @RequestMapping(value = 'tableModels/{key}', method = RequestMethod.DELETE)
    def deleteTableModel(HttpServletRequest request, @PathVariable("key") String key) {
        service.deleteModel(getDb(request), AppProfile.TABLE_TABLE_MODEL, key)
    }

    /*
     * 单据
     */

    // 获取所有单据模型定义
    @RequestMapping(value = 'billModels', method = RequestMethod.GET)
    def billModels(HttpServletRequest request) {
        service.getModels(getDb(request), AppProfile.TABLE_BILL_MODEL)
    }

    // 新建表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.POST, consumes = 'application/json')
    def createBillModel(HttpServletRequest request, @RequestBody Map map) {
        service.createModel(getDb(request), AppProfile.TABLE_BILL_MODEL, map)
    }
    // 更新表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateBillModel(HttpServletRequest request, @RequestBody Map map) {
        service.updateModel(getDb(request), AppProfile.TABLE_BILL_MODEL, map)
    }
    // 删除表模型
    @RequestMapping(value = 'billModels/{key}', method = RequestMethod.DELETE)
    def deleteBillModel(HttpServletRequest request, @PathVariable("key") String key) {
        service.deleteModel(getDb(request), AppProfile.TABLE_BILL_MODEL, key)
    }

    /*
     * 界面
     */

    // 获取所有界面模型定义
    @RequestMapping(value = 'viewModels', method = RequestMethod.GET)
    def viewModels(HttpServletRequest request) {
        service.getViewDatas(getDb(request))
    }

    @RequestMapping(value = 'viewModels/{tid}', method = RequestMethod.GET)
    def viewModelsByTid(HttpServletRequest request, @PathVariable String tid) { // TODO something wrong?
        if (request.getRequestURI() == '/rest/creation/viewModels/ttx.dijit.wso.Bill')
            tid  = 'ttx.dijit.wso.Bill'
        service.getViewDatasByTid(getDb(request),tid)
    }

    // 新建表模型
    @RequestMapping(value = 'viewModels', method = RequestMethod.POST, consumes = 'application/json')
    def createViewModel(HttpServletRequest request, @RequestBody Map map) {
        service.createViewData(getDb(request), map)
    }

    // 更新表模型
    @RequestMapping(value = 'viewModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateViewModel(HttpServletRequest request, @RequestBody Map map) {
        service.updateViewData(getDb(request), map)
    }
    // 表模型
    @RequestMapping(value = 'viewModels/{tid}/{oid}', method = RequestMethod.GET)
    def getViewModel(HttpServletRequest request, @PathVariable String tid, @PathVariable String oid) {
        service.getViewData(getDb(request), tid, oid)
    }
    // 删除表模型
    @RequestMapping(value = 'viewModels/{tid}/{oid}', method = RequestMethod.DELETE)
    def deleteViewModel(HttpServletRequest request, @PathVariable String tid, @PathVariable String oid) {
        service.deleteViewData(getDb(request), tid, oid)
    }

//    /**
//     * 保存结构到数据库
//     */
//    @RequestMapping(value = 'create', method = RequestMethod.POST)
//    @Deprecated
//    Map<String, Object> create(HttpServletRequest request, @RequestBody Map map, BindingResult result) {
//        String sql = "insert into cbt_bill_structure(bill_key, structure) values(?,?)"
//        String billKey = map['billKey']
//        JdbcTemplate template = service.template(getDb(request))
//        int count = template.queryForObject("select count(id) from cbt_bill_structure where bill_key=?", Integer.class, billKey)
//        if (count > 0) {
//            sql = "update cbt_bill_structure set structure=? where bill_key=?"
//        }
//        String structure = (map as JSONObject).toString()
//        template.update(sql, structure, billKey)
//    }
//
//    /**
//     * 从数据库获取结构
//     */
//    @Deprecated
//    @RequestMapping(value = "billStructure/{billKey}", method = RequestMethod.GET)
//    def billStructure(HttpServletRequest request, @PathVariable("billKey") String billKey) {
//        JdbcTemplate template = service.template(getDb(request))
//        String sql = "select structure from cbt_bill_structure where bill_key=?"
//        def json = template.queryForObject(sql, String.class, billKey)
//        json
//    }

}
