package hello.rest_service

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ＠author 王成义 
 * @created 2015-01-15.
 */
@RestController
class IController {
    @RequestMapping('hello')
    def hello(){
        'hello'
    }
}
