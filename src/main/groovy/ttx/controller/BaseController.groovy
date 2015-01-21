package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.support.RequestContextUtils
import ttx.service.base.CreationService

import javax.servlet.http.HttpServletRequest

/**
 * Created by journey on 14-12-6.
 */
@Configuration
class BaseController {

    @Autowired
    CreationService creationService

    // todo delete below
    @RequestMapping(value = 'xxxxx', method = RequestMethod.POST, consumes = 'application/json')
    def xxxxx(HttpServletRequest request, @RequestBody Map map) {
        WebApplicationContext content = RequestContextUtils.getWebApplicationContext(request);
        content = request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

    String getDb(HttpServletRequest request) {
        request.getHeader('X-DB')
    }
}
