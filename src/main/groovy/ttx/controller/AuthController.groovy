package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.service.AuthService

/**
 * ＠author 王成义 
 * @created 2015-01-12.
 */
@Configuration
@RestController
@RequestMapping('/rest/auth')
class AuthController {
    @Autowired
    AuthService service

    @RequestMapping(value = 'login', method = RequestMethod.POST, consumes = 'application/json')
    def login(@RequestBody Map map) {
        String username = map.username
        String token = service.doLogin(username, map.password)
        if (token) {
            [code: 0, user: [username: username, token: token]]
        } else {
            [code: 1]
        }
    }
}
