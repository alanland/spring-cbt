package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.service.AuthService

import javax.servlet.http.HttpServletRequest

/**
 * ＠author 王成义 
 * @created 2015-01-12.
 */
@Configuration
@RestController
@RequestMapping('/rest/auth')
class AuthController extends BaseController{
    @Autowired
    AuthService service

    @RequestMapping(value = 'login', method = RequestMethod.POST, consumes = 'application/json')
    def login(HttpServletRequest request, @RequestBody Map map) {
        String db = getDb(request)
        String username = map.username
        String token = service.doLogin(db, username, map.password)
        if (token) {
            [code: 0, user: [username: username, token: token]]
        } else {
            [code: 1]
        }
    }
}
