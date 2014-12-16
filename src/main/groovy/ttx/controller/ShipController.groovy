package ttx.controller

import com.gemstone.org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.jdbc.JdbcUtil
import ttx.model.outbound.ShipHeader
import ttx.model.outbound.ShipLine
import ttx.service.ShipService

import javax.servlet.http.HttpServletRequest
import java.sql.ResultSet
import java.sql.SQLException
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping('/rest/ship')
class ShipController extends BaseController {
    private final AtomicLong counter = new AtomicLong();
    private final ShipService service = new ShipService()

    ShipService service = new ShipService()

    @RequestMapping(value = 'body', method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> body(HttpEntity<byte[]> requestEntity, @RequestBody JSONObject jsonObj) {
        String requestHeader = requestEntity.getHeaders().getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();

        // do something with request header and body

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = 'body2')
    ResponseEntity<String> body2(HttpEntity<byte[]> requestEntity, @RequestBody Map map) {
        String requestHeader = requestEntity.getHeaders().getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();

        // do something with request header and body

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    /**
     */
    @RequestMapping('list')
    ResponseEntity<Object> list(HttpServletRequest request) {

        def begin = 0, end = 99
        def offset = 0, max = 0
        def sort = '', order = ''
        StringBuilder sql = new StringBuilder("select * from wm_ship_header ")
        request.getParameterMap().each { key, value ->
            def range = request.getHeader('Range') =~ /items=(\d+)-(\d+)/
            if (range.matches()) {
                (begin, end) = [range[0][1] as Integer, range[0][2] as Integer]
            }
            offset = begin
            max = end - begin + 1

            def sortReg = /sort\(([-+ ])(.*)\)/
            request.parameterMap.each { k, v ->
                def m = k =~ sortReg
                if (m.matches()) {
                    sort = m[0][2]
                    order = m[0][1] == '-' ? 'desc' : 'asc'
                } else {
                    if (v && v[0]) {
                        def type = String
                        if (type == String) {
                            sql.append(" $k like %${v[0]}%")
                        } else if (type == java.sql.Date) {
//                                eq "${k}", new java.sql.Date(Date.parse('yyyy-MM-dd', v[0]).getTime())
                        } else if (type in [Long, Integer, BigDecimal, Double]) {
//                                eq "$k", v[0]
                        } else {// TODO 其他类型的支持
                        }
                    }
                }
            }
        }
        JdbcTemplate template = JdbcUtil.getTemplate()
        def res = template.query(
                sql.toString(),
                new RowMapper() {
                    @Override
                    Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        [
                                id   : rs.getInt('id'),
                                no   : rs.getString('sh_no'),
                                owner: rs.getString('sh_owner'),
                                count: rs.getInt('sh_count')
                        ]
                    }
                }
        )

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Range", "items $begin-$end/${res.size() + 1000}");
        new ResponseEntity(res, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping('list2')
    List<ShipHeader> list2() {
        def list = []
        for (i in 1..100) {
            list.add(new ShipHeader(
                    id: counter.incrementAndGet(),
                    no: System.currentTimeMillis().toString(),
                    owner: "ower$i",
                    count: Math.random() * 30 as int
            ))
        }
        list
    }

    @RequestMapping('detailList')
    def detailList() {
        def list = []
        def headerId = Math.random() * 30 + 3000
        for (i in 1..100) {
            list.add(new ShipLine(
                    id: counter.incrementAndGet(),
                    headerId: headerId,
                    headerNo: "headerNo:$i",
                    material: "material:$i",
                    spec: "spec:$i",
                    qty: Math.random() * 30 as int
            ))
        }
        list
    }

    @RequestMapping('maptest')
    def maptest() {
        [
                "key1": "value1",
                "key2": "value2",
                "key3": "value3",
                "key4": "value4",
        ]
    }
}
