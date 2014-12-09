package ttx.controller.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.jdbc.JdbcUtil

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

@RestController
@RequestMapping('/rest/demo/user')
class UserController {

    static JdbcTemplate template = JdbcUtil.getTemplate()
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    @RequestMapping('list')
    def list(HttpServletRequest req) {
        listData(
                req.getParameter('username') ?: '',
                req.getParameter('usercode') ?: '',
                req.getParameter('userdepartment') ?: ''
        )
    }

    def listData(String name, String code,String dep){
        String sql = """
select * from ttx_user where
    us_username like '%${name}%'
    and us_usercode like '%${code}%'
    and us_department like '%${dep}%'
  order by us_username
"""
        def res = template.queryForList(sql)
        res
    }

    @RequestMapping(value = '', method = RequestMethod.DELETE, consumes = 'application/json')
    def delete(HttpServletRequest req,@RequestBody List<Integer> list) {
        String sql = "delete from ttx_user where id in(${list.join(',')})"
        template.update(sql)
        listData(
                req.getParameter('username') ?: '',
                req.getParameter('usercode') ?: '',
                req.getParameter('userdepartment') ?: ''
        )
    }

    @RequestMapping(value = '', method = RequestMethod.PUT, consumes = 'application/json')
    def put(@RequestBody Map map) {
        def id = map.get('id', -1)
        int count = template.queryForObject('select count(id) from ttx_user where id=?', Integer.class, id)
        String sql = '''
update ttx_user set
    us_usercode=?,
    us_username=?,
    us_department=?,
    us_password=?,
    us_role=?,
    us_active=?,
    us_start_date=?,
    us_end_date=?,
    us_allow_login=?
where id=''' + id
        if (count == 0) {
            sql = '''
insert into ttx_user(
us_usercode,us_username,us_department,us_password,us_role,us_active,us_start_date,us_end_date,us_allow_login
) values (?,?,?,?,?,?,?,?,?)
'''
        }
        String code = map.get('us_usercode', '')
        String name = map.get('us_username', '')
        String department = map.get('us_department', '')
        String password = map.get('us_password', '')
        String role = map.get('us_role', 'User')
        Integer active = map.get('us_active', 1) as Integer
        Date startDate = dateFormat.parse(map.get('us_start_date').toString())
        Date endDate = dateFormat.parse(map.get('us_end_date').toString())
        Integer allowLogin = map.get('us_allow_login', 0)
        template.update(
                sql,
                code, name, department, password, role, active, startDate, endDate, allowLogin
        )
    }

    @RequestMapping(value = 'body', method = RequestMethod.POST, consumes = "application/json")
    def test() {
        'abc'
    }


}
