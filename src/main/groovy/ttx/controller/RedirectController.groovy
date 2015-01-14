package ttx.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ＠author 王成义 
 * @created 2015-01-12.
 */
@RestController
@RequestMapping('/rest/redirect')
class RedirectController {
    @RequestMapping('error')
    def error(){
        [code:'1',desc:'error']
    }
}
