package ttx.controller

import org.springframework.web.bind.annotation.RequestMapping
import ttx.service.base.CreationService

/**
 * Created by journey on 14-12-6.
 */
abstract class BaseController {

    abstract CreationService getService();

    @RequestMapping('listAll')
    def listAll() {
        getService().getList()
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
    def headerFieldData(){
        service.getHeaderFieldData()
    }

    /**
     * line structure data
     */
    @RequestMapping('lineStructureData')
    def lineStructureData(){
        service.getLineStructureData()
    }

    /**
     * line field data
     */
    @RequestMapping('lineFieldData')
    def lineFieldData(){
        service.getLineFieldData()
    }
}
