package ttx.controller

import com.gemstone.org.json.JSONObject
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ttx.service.base.BaseService
import ttx.service.base.RegistryCenter

/**
 * Created by journey on 14-12-5.
 */
@RestController
@RequestMapping('/rest/creation')
class CreationController {
    private final BaseService service = new BaseService()

    @RequestMapping('billMapping')
    def billMapping() {
        RegistryCenter.getMapping()
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
