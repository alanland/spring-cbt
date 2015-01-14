package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
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
    CreationService service

    // todo delete below

    @RequestMapping('xxxxxx')
    def xxxxx(HttpServletRequest request) {
        WebApplicationContext content = RequestContextUtils.getWebApplicationContext(request);
        content = request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

    @Deprecated
    @RequestMapping('listAll')
    def listAll() {
        service.getList()
    }

    /**
     * get query field list data
     */
    @RequestMapping('queryFieldData')
    def queryFieldData() {
        service.getQueryFieldStructureData()
    }

    /**
     * list structure data
     */
    @RequestMapping('listStructureData')
    def listStructureData() {
        service.getListStructureData()
    }

    /**
     * header field structure data
     */
    @RequestMapping('headerFieldData')
    def headerFieldData() {
        service.getHeaderFieldData()
    }

    /**
     * line structure data
     */
    @RequestMapping('lineStructureData')
    def lineStructureData() {
        service.getLineStructureData()
    }

    /**
     * line field data
     */
    @RequestMapping('lineFieldData')
    def lineFieldData() {
        service.getLineFieldData()
    }
}
