package ttx.controller

import com.gemstone.org.json.JSONObject
import groovy.json.JsonSlurper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ttx.service.RegistryCenter
import ttx.service.base.CreationService

/**
 * Created by journey on 14-12-5.
 */
@RestController
@RequestMapping('/rest/creation')
class CreationController {
    private final CreationService service = new CreationService()

    @RequestMapping('billMapping')
    @Deprecated
    def billMapping() {
        RegistryCenter.getMapping()
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

    @RequestMapping(value = 'tables/{table}', method = RequestMethod.POST)
    def createTableModel(@PathVariable("table") table, @RequestBody Map map) {

    }


    /*
     * 表模型
     */

    // 获取所有表模型定义
    @RequestMapping(value='tableModels',method=RequestMethod.GET)
    def tableModels(){
        def list = service.getTemplate().queryForList('select * from ttx_table_model')
        list.collect { Map map->
            new JsonSlurper().parseText(map.structure) as Map
        }
    }

    // 新建表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.POST, consumes = 'application/json')
    def createTableModel(@RequestBody Map map) {
        JdbcTemplate template = service.getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ttx_table_model where key=?", Integer.class, key)
        if (count > 0)
            return [code: '1', desc: "key [${key}] existed"]
        String sql = "insert into ttx_table_model(version,key,structure) values(1,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, key, (map as JSONObject).toString())
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }
    // 更新表模型
    @RequestMapping(value = 'tableModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateTableModel(@RequestBody Map map) {
        JdbcTemplate template = service.getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ttx_table_model where key=?", Integer.class, key)
        if (count != 0)
            return [code: '1', desc: "key [${key}] not existed"]
        String sql = "update ttx_table_model set structure=? where key=?"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, (map as JSONObject).toString(), key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }

    // 删除表模型
    @RequestMapping(value='tableModels/{table}',method=RequestMethod.DELETE)
    def deleteTableModel(@PathVariable("table") String table){
        def code = '0', desc = 'ok'
        try {
            service.getTemplate().update('delete from ttx_table_model where key=?', table)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }


    /*
     * 单据
     */

    // 获取所有单据模型定义
    @RequestMapping(value='billModels',method=RequestMethod.GET)
    def billModels(){
        def list = service.getTemplate().queryForList('select * from ttx_bill_model')
        list.collect { Map map->
            new JsonSlurper().parseText(map.structure) as Map
        }
    }

    // 新建表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.POST, consumes = 'application/json')
    def createBillModel(@RequestBody Map map) {
        JdbcTemplate template = service.getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ttx_bill_model where key=?", Integer.class, key)
        if (count > 0)
            return [code: '1', desc: "key [${key}] existed"]
        String sql = "insert into ttx_bill_model(version,key,structure) values(1,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, key, (map as JSONObject).toString())
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }
    // 更新表模型
    @RequestMapping(value = 'billModels', method = RequestMethod.PUT, consumes = 'application/json')
    def updateBillModel(@RequestBody Map map) {
        JdbcTemplate template = service.getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ttx_bill_model where key=?", Integer.class, key)
        if (count != 0)
            return [code: '1', desc: "key [${key}] not existed"]
        String sql = "update ttx_bill_model set structure=? where key=?"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, (map as JSONObject).toString(), key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }
    // 删除表模型
    @RequestMapping(value='billModels/{bill}',method=RequestMethod.DELETE)
    def deleteBillModel(@PathVariable("bill") String bill){
        def code = '0', desc = 'ok'
        try {
            service.getTemplate().update('delete from ttx_bill_model where key=?', bill)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }




    /**
     * 保存结构到数据库
     */
    @RequestMapping(value = 'create', method = RequestMethod.POST)
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
    @RequestMapping(value = "billStructure/{billKey}", method = RequestMethod.GET)
    def billStructure(@PathVariable("billKey") String billKey) {
        JdbcTemplate template = service.getTemplate()
        String sql = "select structure from cbt_bill_structure where bill_key=?"
        def json = template.queryForObject(sql, String.class, billKey)
        json
    }

}
