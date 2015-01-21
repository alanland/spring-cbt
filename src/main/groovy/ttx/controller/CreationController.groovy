package ttx.controller

import com.gemstone.org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ttx.service.base.CreationService
import ttx.util.ModelCache

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
//    // TODO to delete
//    @RequestMapping(value = 'navigator', method = RequestMethod.GET)
//    def navigator() {
//        service.getSysNavigator()
//    }
//
//    @RequestMapping(value = 'navigator/{key}', method = RequestMethod.GET)
//    def navigatorGet(@PathVariable String key) {
//        service.getNavigator(key)
//    }
//
//    // 更新表模型 todo 权限
//    @RequestMapping(value = 'navigator', method = RequestMethod.PUT, consumes = 'application/json')
//    def updateNavigator(@RequestBody Map map) {
//        service.updateNavigator('admin', map.data)
//    }

    // 更新表模型 todo 权限
    @RequestMapping(value = 'navigator/{key}', method = RequestMethod.PUT, consumes = 'application/json')
    def updateNavigator(@PathVariable String key, @RequestBody Map map) {
        service.updateNavigator(key, map.data)
    }

    // 获取数据库所有表
    @RequestMapping('tables')
    def tables() {
        def list = []
        service.getTables().each {
            list.add(id: it, name: it)
        }
        list
    }

    // 获取所有字段
    @RequestMapping('tables/{table}/fields')
    def fields(@PathVariable("table") String table) {
        service.getFields(table)
    }

    /*
     * 表模型
     */

    // 获取所有表模型定义
    @RequestMapping(value = 'tableModels', method = RequestMethod.GET)
    def tableModels() {
        service.getModels(ModelCache.TABLE_TABLE_MODEL)
    }

    // 新建表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.POST, consumes = 'application/json')
    def createTableModel(@RequestBody Map map) {
        service.createModel(ModelCache.TABLE_TABLE_MODEL, map)
    }
    // 更新表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateTableModel(@RequestBody Map map) {
        service.updateModel(ModelCache.TABLE_TABLE_MODEL, map)
    }

    // 删除表模型
    @RequestMapping(value = 'tableModels/{key}', method = RequestMethod.DELETE)
    def deleteTableModel(@PathVariable("key") String key) {
        service.deleteModel(ModelCache.TABLE_TABLE_MODEL, key)
    }

    /*
     * 单据
     */

    // 获取所有单据模型定义
    @RequestMapping(value = 'billModels', method = RequestMethod.GET)
    def billModels() {
        service.getModels(ModelCache.TABLE_BILL_MODEL)
    }

    // 新建表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.POST, consumes = 'application/json')
    def createBillModel(@RequestBody Map map) {
        service.createModel(ModelCache.TABLE_BILL_MODEL, map)
    }
    // 更新表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateBillModel(@RequestBody Map map) {
        service.updateModel(ModelCache.TABLE_BILL_MODEL, map)
    }
    // 删除表模型
    @RequestMapping(value = 'billModels/{key}', method = RequestMethod.DELETE)
    def deleteBillModel(@PathVariable("key") String key) {
        service.deleteModel(ModelCache.TABLE_BILL_MODEL, key)
    }

    /*
     * 界面
     */

    // 获取所有界面模型定义
    @RequestMapping(value = 'viewModels', method = RequestMethod.GET)
    def viewModels() {
        service.getModels(ModelCache.TABLE_VIEW_MODEL)
    }

    // 新建表模型
    @RequestMapping(value = 'viewModels', method = RequestMethod.POST, consumes = 'application/json')
    def createViewModel(@RequestBody Map map) {
        service.createModel(ModelCache.TABLE_VIEW_MODEL, map)
    }

    // 更新表模型
    @RequestMapping(value = 'viewModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateViewModel(@RequestBody Map map) {
        service.updateModel(ModelCache.TABLE_VIEW_MODEL, map)
    }
    // 表模型
    @RequestMapping(value = 'viewModels/{view}', method = RequestMethod.GET)
    def getViewModel(@PathVariable("view") String bill) {
        service.getModel(ModelCache.TABLE_VIEW_MODEL, bill)
    }
    // 删除表模型
    @RequestMapping(value = 'viewModels/{view}', method = RequestMethod.DELETE)
    def deleteViewModel(@PathVariable("view") String key) {
        service.deleteModel(ModelCache.TABLE_VIEW_MODEL, key)
    }

    /**
     * 保存结构到数据库
     */
    @RequestMapping(value = 'create', method = RequestMethod.POST)
    @Deprecated
    Map<String, Object> create(@RequestBody Map map, BindingResult result) {
        String sql = "insert into cbt_bill_structure(bill_key, structure) values(?,?)"
        String billKey = map['billKey']
        JdbcTemplate template = service.getTemplate()
        int count = template.queryForObject("select count(id) from cbt_bill_structure where bill_key=?", Integer.class, billKey)
        if (count > 0) {
            sql = "update cbt_bill_structure set structure=? where bill_key=?"
        }
        String structure = (map as JSONObject).toString()
        template.update(sql, structure, billKey)
    }

    /**
     * 从数据库获取结构
     */
    @Deprecated
    @RequestMapping(value = "billStructure/{billKey}", method = RequestMethod.GET)
    def billStructure(@PathVariable("billKey") String billKey) {
        JdbcTemplate template = service.getTemplate()
        String sql = "select structure from cbt_bill_structure where bill_key=?"
        def json = template.queryForObject(sql, String.class, billKey)
        json
    }

}
