package ttx.controller

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*
import ttx.jdbc.rest.page.PostgresPagination
import ttx.service.RestTableService
import ttx.util.ModelCache

import javax.servlet.http.HttpServletRequest

/**
 * ＠author 王成义
 *
 * @created 2015-01-03.
 */
//@Configuration // todo does not need
@RestController
@RequestMapping('/rest/cbt')
class RestTableController extends BaseController {
    @Autowired
    RestTableService service

    @RequestMapping(value = '{tableKey}', method = RequestMethod.POST, consumes = 'application/json')
    def create(HttpServletRequest request, @PathVariable("tableKey") String tableKey, @RequestBody Map map) {
        service.create(getDb(request), tableKey, map)
        [code: 0]
    }

    @RequestMapping(value = '{tableKey}', method = RequestMethod.PUT, consumes = 'application/json')
    def update(HttpServletRequest request, @PathVariable("tableKey") String tableKey, @RequestBody Map map) {
        service.saveOrUpdate(getDb(request), tableKey, map)
    }

    @RequestMapping(value = '{tableKey}/{id}', method = RequestMethod.GET)
    def get(@PathVariable("tableKey") String tableKey, @PathVariable("id") String idStr, HttpServletRequest request) {
        service.get(getDb(request), tableKey, idStr)
    }

    @RequestMapping(value = 'filteringSelect/{tableKey}', method = RequestMethod.GET)
    def filteringSelect(@PathVariable("tableKey") String tableKey, HttpServletRequest request) {
        service.filterSelect(getDb(request), tableKey)
    }

    @RequestMapping(value = '{tableKey}', method = RequestMethod.GET)
    def getList(@PathVariable("tableKey") String tableKey, HttpServletRequest request) {
        // 根据key名称获得表名称
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        String tableName = tableModel.tableName
        String bill = request.getHeader('X-Bill') // 是否是单据的查询，可能多表
        // 返回查询字段，如果客户端有指定，返回指定的
        String content = 'h.*'
        String resultFields = request.getHeader('X-Result-Fields')
        if (resultFields) {
            content = resultFields.split(',').collect({
                'h.' + it
            }).join(',')
        }

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
        JdbcTemplate template = service.template(getDb(request))
//        def res = template.queryForMap(sql.replace('{content}', content), values.toArray())
        def page = new PostgresPagination(sql.toString().replace('{content}', content), template, begin, end, values.toArray())
        def count = template.queryForObject(sql.toString().replace('{content}', 'count(1)'), Integer.class, values.toArray())
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
    def deleteTable(HttpServletRequest request, @PathVariable('tableKey') String tableKey, @RequestBody Map map) {
        service.deleteTable(getDb(request), tableKey, map)
    }
}
