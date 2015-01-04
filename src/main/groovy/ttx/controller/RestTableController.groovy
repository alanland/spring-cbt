package ttx.controller

import groovy.json.JsonSlurper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*
import ttx.jdbc.JdbcUtil
import ttx.jdbc.rest.page.PostgresPagination
import ttx.service.base.CreationService
import ttx.util.SequenceGenerator

import javax.servlet.http.HttpServletRequest

/**
 * ＠author 王成义
 *
 * @created 2015-01-03.
 */
@RestController
@RequestMapping('/rest/cbt')
class RestTableController {
    private final CreationService service = new CreationService()

    @RequestMapping(value = '{tableKey}', method = RequestMethod.POST, consumes = 'application/json')
    def create(@PathVariable("tableKey") String tableKey, @RequestBody Map map) {
        Map tableModel = service.getCachedModel(service.TABLE_TABLE_MODEL, tableKey)
        map[tableModel.idColumnName] = SequenceGenerator.next()
        JdbcUtil.getInsert().withTableName(tableModel.tableName).execute(map)
        [code: 0]
    }

    @RequestMapping(value = '{tableKey}', method = RequestMethod.PUT, consumes = 'application/json')
    def update(@PathVariable("tableKey") String tableKey, @RequestBody Map map) {
        Map tableModel = service.getCachedModel(service.TABLE_TABLE_MODEL, tableKey)
        JdbcTemplate template = JdbcUtil.getTemplate()
        def id = map[tableModel.idColumnName]
        int count = template.queryForObject(
                "select count(1) from ${tableModel.tableName} where ${tableModel.idColumnName}=?",
                Integer.class, id
        )
        if (count != 1) {
            return [code: 1, desc: "$count items exist in ${tableModel.tableName}"]
        }
        def where = []
        where[tableModel.idColumnName] = map.get(tableModel.idColumnName)
        map.remove(tableModel.idColumnName)
        JdbcUtil.getUpdate().withTableName(tableModel.tableName).execute(map, where)
        [code: 0]
    }

    @RequestMapping(value = '{tableKey}/{id}', method = RequestMethod.GET)
    def get(@PathVariable("tableKey") String tableKey, @PathVariable("id") String idStr, HttpServletRequest request) {
        Map tableModel = service.getCachedModel(service.TABLE_TABLE_MODEL, tableKey)
        JdbcTemplate template = JdbcUtil.getTemplate()
        def item = tableModel.fields.find {
            it.field == tableModel.idColumnName
        }
        def id = item.type == 'integer' ? Long.valueOf(idStr) : idStr
        template.queryForMap("select * from ${tableModel.tableName} where ${tableModel.idColumnName}=?", id)
    }


    @RequestMapping(value = '{tableKey}', method = RequestMethod.GET)
    def getList(@PathVariable("tableKey") String tableKey, HttpServletRequest request) {
        // 根据key名称获得表名称
        Map tableModel = service.getCachedModel(service.TABLE_TABLE_MODEL, tableKey)
        String tableName = tableModel.tableName
        String bill = request.getHeader('X-Bill') // 是否是单据的查询，可能多表

        // 查询范围
        def begin = 0, end = 99
        def offset = 0, max = 0
        def sort = '', order = ''
        def range = request.getHeader('Range') =~ /items=(\d+)-(\d+)/
        if (range.matches()) {
            (begin, end) = [range[0][1] as Integer, range[0][2] as Integer]
        }
        offset = begin
        max = end - begin + 1

        // filter
        def content = 'h.*'
        StringBuilder sql = new StringBuilder("select {content} from ${tableName} h ")
        def where = new StringBuilder(' where 1=1 ')
        def filter = request.getHeader('filter')
        def values = []
        if (filter) {
            if (bill) { // 多表查询
                def headerWhere = new StringBuilder()
                def headerValues = []
                def detailWhere = new StringBuilder()
                def detailValues = []
                def json = new JsonSlurper().parseText(filter)
                json['and'].each { c ->
                    def tmpWhere, tmpValues, alis
                    boolean isHeader = c.table == tableKey // todo 单据连表
                    tmpWhere = isHeader ? headerWhere : detailWhere
                    tmpValues = isHeader ? headerValues : detailValues
                    alis = isHeader ? 'h.' : 'd.'
                    tmpWhere.append(" and ${alis}${c.field} ${c.operator} ? ")
                    if (c.operator == 'like') { // todo ->operator
                        tmpValues.add("%${c.value}%")
                    } else {
                        tmpValues.add(c.value)
                    }
                }
                if (detailValues) {
                    sql = 'select {content} from ${headerTableName} h ' // todo
                }
                sql.append where
                sql.append headerWhere
                sql.append detailWhere
                values = headerValues + detailValues
            } else { // 单表查询
                def json = new JsonSlurper().parseText(filter)
                json['and'].each { c ->
                    where.append(" and ${c.field} ${c.operator ?: '='} ? ")
                    if (c.operator == 'like') { // todo ->operator
                        values.add("%${c.value}%")
                    } else {
                        values.add(c.value)
                    }
                }
                sql.append where
            }
        }

        // 排序
        def sortReg = /sort\(([-+ ])(.*)\)/
        request.parameterMap.each { k, v ->
            def m = k =~ sortReg
            if (m.matches()) {
                sort = m[0][2]
                order = m[0][1] == '-' ? 'desc' : 'asc'
//            } else {
//                if (v && v[0]) {
//                    def type = String
//                    if (type == String) {
//                        sql.append(" $k like %${v[0]}%")
//                    } else if (type == java.sql.Date) {
////                                eq "${k}", new java.sql.Date(Date.parse('yyyy-MM-dd', v[0]).getTime())
//                    } else if (type in [Long, Integer, BigDecimal, Double]) {
////                                eq "$k", v[0]
//                    } else {// TODO 其他类型的支持
//                    }
//                }
            }
        }
        // 分页数据
        JdbcTemplate template = JdbcUtil.getTemplate()
//        def res = template.queryForMap(sql.replace('{content}', content), values.toArray())
        def count = template.queryForObject(sql.toString().replace('{content}', 'count(1)'), Integer.class, values.toArray())
        def page = new PostgresPagination(sql.toString().replace('{content}', content), template, begin, end, values.toArray())
//        def res = template.query(  // todo 参考代码
//                sql.toString(),
//                new RowMapper() {
//                    @Override
//                    Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        [
//                                id   : rs.getInt('id'),
//                                no   : rs.getString('sh_no'),
//                                owner: rs.getString('sh_owner'),
//                                count: rs.getInt('sh_count')
//                        ]
//                    }
//                }
//        )

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Range", "items $begin-$end/${count}");
        new ResponseEntity(page.getRows(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = '{tableKey}', method = RequestMethod.DELETE, consumes = 'application/json')
    def deleteTable(@PathVariable('tableKey') String tableKey, @RequestBody Map map) {
        List list = map.items
        Map tableModel = service.getCachedModel(service.TABLE_TABLE_MODEL, tableKey)
        String idColumn = tableModel.idColumnName
        def item = tableModel.fields.find {
            it.field == idColumn
        }
        if (item.type == 'string')
            list = list.collect {
                "'$it'"
            }
        String sql = "delete from ${tableModel.tableName} where ${idColumn} in(${list.join(',')})"
        JdbcUtil.getTemplate().update(sql)
        [code: 0]
    }

}
