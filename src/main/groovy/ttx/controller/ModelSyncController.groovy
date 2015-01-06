package ttx.controller

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ttx.service.base.CreationService

/**
 * ＠author 王成义
 *
 * @created 2015-01-06.
 */
@RestController
@RequestMapping('/rest/modelSync')
class ModelSyncController {
    private final CreationService service = new CreationService()
    private final String filePath = "/home/journey/day/svn/cbt/src/main/resources/config/sync"

    @RequestMapping(value = 'databaseToFile', method = RequestMethod.POST, consumes = 'application/json')
    def databaseToFile() {
        String backupPath = "${filePath}_${new Date().format('yyyy-MM-dd_hh_mm_ss')}"
        [CreationService.TABLE_TABLE_MODEL, CreationService.TABLE_BILL_MODEL, CreationService.TABLE_VIEW_MODEL].each { table ->
            File dir = new File(filePath, table)
            File backupDir = new File(backupPath, table)
            if (!dir.exists()) dir.mkdirs()
            if (!backupDir.exists()) backupDir.exists()
            service.getModels(table).each { model ->
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
        [code: 0]
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
