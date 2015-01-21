package ttx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ttx.service.PermissionService

import javax.servlet.http.HttpServletRequest

/**
 * ＠author 王成义 
 * @created 2015-01-19.
 */
@RestController
@RequestMapping('/rest/permission')
class PermissionController extends BaseController {

    @Autowired
    PermissionService service

    @RequestMapping(value = 'nav/{role}', method = RequestMethod.GET)
    def getNav(HttpServletRequest request, @PathVariable String role) { // 全部，包含状态的
        String db = getDb(request)
        service.getEditableNav(db, role)
    }

    @RequestMapping(value = 'securableNav/{role}', method = RequestMethod.GET)
    def getSecurableNav(HttpServletRequest request, @PathVariable String role) { // 部分状态
        String db = getDb(request)
        service.getSecurableNav(db, role)
    }

    @RequestMapping(value = 'nav/{role}', method = RequestMethod.POST, consumes = 'application/json')
    def createRoleNav(HttpServletRequest request, @PathVariable String role, @RequestBody Map map) {
        String db = getDb(request)
        service.createOrUpdateNav(db, role, map.items)
    }

    @RequestMapping(value = 'nav/{role}/{view}', method = RequestMethod.GET)
    def getViewActions(HttpServletRequest request, @PathVariable String role, @PathVariable String view) {
        String db = getDb(request)
        service.getEditableActions(db, role, view)
    }

    @RequestMapping(value = 'nav/{role}/{view}', method = RequestMethod.POST, consumes = 'application/json')
    def saveViewActions(HttpServletRequest request,
                        @PathVariable String role, @PathVariable String view, @RequestBody Map map) {
        String db = getDb(request)
        service.createOrUpdateActions(db, role, view, map.items)
    }
}
