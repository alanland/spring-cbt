package ttx.service.base

import com.gemstone.org.json.JSONObject
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData
import ttx.model.meta.FieldType
import ttx.model.meta.MetaTable

/**
 * Created by journey on 14-12-6.
 */
class CreationService extends BaseService {

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
        list
    }

    // 表字段
    def getFields(String table) {
        SqlRowSet rowSet = template.queryForRowSet("select * from $table where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).collect { index ->
            String columnName = meta.getColumnName(index)
            [
                    "id"   : Math.random(),
                    'field': columnName,
                    'name' : columnName,
                    'type' : FieldType.getType(meta.getColumnTypeName(index)) // TODO
            ]
        }
    }

    // 新增一条记录到表 table_model
    def saveModel(String key, Map map) {
        String sql = "insert into ttx_table_model(version,key,structure) values(?,?,?)"
        getTemplate().update(sql, MetaTable.VERSION, key, (map as JSONObject).toString())
    }

/**
 * Query field structure data
 */
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
