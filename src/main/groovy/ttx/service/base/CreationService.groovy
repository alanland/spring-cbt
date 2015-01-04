package ttx.service.base

import com.gemstone.org.json.JSONObject
import groovy.json.JsonSlurper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData
import ttx.model.meta.FieldType
import ttx.model.meta.MetaTable

/**
 * Created by journey on 14-12-6.
 */
class CreationService extends BaseService {

    static final TABLE_TABLE_MODEL = "ttx_table_model"
    static final TABLE_BILL_MODEL = "ttx_bill_model"
    static final TABLE_VIEW_MODEL = "ttx_view_model"

    static Map cache = new HashMap()

    CreationService() {
        super()
    }

    // 所有表名称
    def getTables() {
        def sql = '''
select relname as TABLE_NAME ,col_description(c.oid, 0) as COMMENTS from pg_class c
where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname
'''
        sql = '''
select relname as TABLE_NAME from pg_class c
where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname
'''
        List<String> list = template.queryForList(sql, String.class)
        cache.tables = list
        list
    }

    // 表字段
    def getFields(String table) {
        SqlRowSet rowSet = template.queryForRowSet("select * from $table where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).collect { index ->
            String columnName = meta.getColumnName(index)
            [
                    "id"   : columnName,
                    'field': columnName,
                    'name' : columnName,
                    'type' : FieldType.getType(meta.getColumnTypeName(index)) // TODO
            ]
        }
    }

    def getModel(String table, String key) {
        getTemplate().queryForMap("select * from ${table} where key=?", key).structure
    }

    def getModels(String table) {
        def list = getTemplate().queryForList("select * from $table")
        def models = list.collect { Map map ->
            new JsonSlurper().parseText(map.structure) as Map
        }
        cache[table] = models
        models
    }

    def createModel(String table, Map map) {
        JdbcTemplate template = getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ${table} where key=?", Integer.class, key)
        if (count > 0)
            return [code: '1', desc: "key [${key}] existed"]
        String sql = "insert into ${table}(version,key,structure) values(1,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, key, (map as JSONObject).toString())
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getModels(table)//    update cache    #todo to change
        [code: code, desc: desc]
    }

    def updateModel(String table, Map map) {
        JdbcTemplate template = getTemplate()
        String key = map['key']
        int count = template.queryForObject("select count(key) from ${table} where key=?", Integer.class, key)
        if (count == 0)
            return [code: '1', desc: "key [${key}] not existed in ${table}"]
        if (count > 1)
            return [code: '1', desc: "key [${key}] more than one items in ${table}"]
        String sql = "update ${table} set structure=? where key=?"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, (map as JSONObject).toString(), key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getModels(table)//    update cache    #todo to change
        return [code: code, desc: desc]
    }

    def deleteModel(String table, String key) {
        def code = '0', desc = 'ok'
        try {
            getTemplate().update("delete from ${table} where key=?", key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getModels(table)//    update cache    #todo to change
        return [code: code, desc: desc]
    }

    def buildCache(){
        [TABLE_TABLE_MODEL,TABLE_BILL_MODEL,TABLE_VIEW_MODEL].each{
            getModels(it)
        }
    }

    def getCachedModel(String table, String key) {
        cache[table].find {
            it.key == key
        }
    }

    // 新增一条记录到表 table_model
    // TODO vesion
    @Deprecated
    def saveModel(String key, Map map) {
        String sql = "insert into ttx_table_model(version,key,structure) values(?,?,?)"
        getTemplate().update(sql, MetaTable.VERSION, key, (map as JSONObject).toString())
    }

/**
 * Query field structure data
 */
    @Deprecated
    def getQueryFieldStructureData() {
        def list = []
        SqlRowSet rowSet = template.queryForRowSet("select * from ${header.headerTableName} where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (header.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = line.tableFieldNameMapping[columnName]
                map['type'] = 'string'//meta.getColumnTypeName(index)
                map['location'] = 'Header'
                map['operator'] = 'equals'
                list.add map
            }
        }

        rowSet = template.queryForRowSet("select * from ${header.lineTableName} where 1=2 ")
        meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (line.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = line.tableFieldNameMapping[columnName]
                map['type'] = meta.getColumnTypeName(index)
                map['location'] = 'Line'
                list.add map
            }
        }
        list
    }

/**
 * list structure data
 */
    @Deprecated
    def getListStructureData() {
        def list = [
                [id: Math.random(), field: 'bill_id', name: 'BillId', width: '80px'],
                [id: Math.random(), field: 'bill_no', name: 'BillNo', width: '80px']
        ]
        SqlRowSet rowSet = template.queryForRowSet("select * from ${header.headerTableName} where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (header.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = line.tableFieldNameMapping[columnName]
                map['type'] = meta.getColumnTypeName(index)
                map['width'] = '80px'
                list.add map
            }
        }
        list
    }

/**
 * bill header field
 */
    @Deprecated
    def getHeaderFieldData() {
        def list = []
        SqlRowSet rowSet = template.queryForRowSet("select * from ${header.headerTableName} where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (header.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = header.tableFieldNameMapping[columnName]
                map['type'] = 'string'//meta.getColumnTypeName(index)
                list.add map
            }
        }
        list
    }

/**
 * details grid structure data
 */
    @Deprecated
    def getLineStructureData() {
        def list = [
                [id: Math.random(), field: 'line_id', name: 'LineId', width: '80px'],
                [id: Math.random(), field: 'bill_id', name: 'BillId', width: '80px'],
                [id: Math.random(), field: 'bill_no', name: 'BillNo', width: '80px']
        ]
        SqlRowSet rowSet = template.queryForRowSet("select * from ${header.headerTableName} where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (header.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = line.tableFieldNameMapping[columnName]
                map['type'] = meta.getColumnTypeName(index)
                map['width'] = '80px'
                list.add map
            }
        }
        list
    }

/**
 * get detail field structure data
 */
    @Deprecated
    def getLineFieldData() {
        def list = []
        SqlRowSet rowSet = template.queryForRowSet("select * from ${header.headerTableName} where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).each { index ->
            String columnName = meta.getColumnName(index)
            if (header.tableFieldNameMapping.containsKey(columnName)) {
                def map = [id: Math.random()]
                map['field'] = columnName
                map['name'] = line.tableFieldNameMapping[columnName]
                map['type'] = 'string'//meta.getColumnTypeName(index)
                list.add map
            }
        }
        list
    }


}
