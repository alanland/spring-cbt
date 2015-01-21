package ttx.controller

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.service.base.CreationService
import ttx.util.config.AppProfile

import javax.servlet.http.HttpServletRequest

/**
 * ＠author 王成义
 *
 * @created 2015-01-06.
 */
@Configuration
@RestController
@RequestMapping('/rest/modelSync')
class ModelSyncController extends BaseController {
    @Autowired
    CreationService service

    // todo 路径
    private final String filePath = "/home/journey/day/svn/cbt/src/main/resources/config/sync"

    @RequestMapping(value = 'databaseToFile', method = RequestMethod.POST, consumes = 'application/json')
    def databaseToFile(HttpServletRequest request) {
        String db = getDb(request)
        writeModelsToFile(db)
        writeNavigatorToFile(db)
        [code: 0]
    }

    def writeNavigatorToFile(String db) {
        String backupPath = "${filePath}/${db}/${new Date().format('yyyy-MM-dd_hh_mm_ss')}"
        File dir = new File(filePath)
        File backupDir = new File(backupPath)
        if (!dir.exists()) dir.mkdirs()
        if (!backupDir.exists()) backupDir.mkdirs()
        service.get

    }

    def writeModelsToFile(String db) {
        writeModelToFile(db, AppProfile.TABLE_TABLE_MODEL)
        writeModelToFile(db, AppProfile.TABLE_BILL_MODEL)
    }

    def writeModelToFile(String db, String table) {
        String backupPath = "${filePath}/${db}/${new Date().format('yyyy-MM-dd_hh_mm_ss')}"
        File dir = new File(filePath, table)
        File backupDir = new File(backupPath, table)
        if (!dir.exists()) dir.mkdirs()
        if (!backupDir.exists()) backupDir.mkdirs()
        service.getModels(db, table).each { model ->
            def json = JsonOutput.prettyPrint(JsonOutput.toJson(model))
            new File(dir, "${model.key}.json").withPrintWriter { out ->
                out.println(json)
            }
            // backup
            new File(backupDir, "${model.key}.json").withPrintWriter { out ->
                out.println(json)
            }
        }
    }

    @RequestMapping(value = 'fileToDatabase', method = RequestMethod.POST, consumes = 'application/json')
    def update() {
        [CreationService.TABLE_TABLE_MODEL, CreationService.TABLE_BILL_MODEL, CreationService.TABLE_VIEW_MODEL].each { table ->
            File dir = new File(filePath, table)
            if (dir.isDirectory()) {
                dir.eachFile { File file ->
                    println new JsonSlurper().parse(file).toString()
                }
            }
        }
        [code: 0]
    }


}
