package ttx.model

import groovy.json.JsonSlurper
import ttx.model.meta.Field
import ttx.model.meta.FieldType
import ttx.model.meta.MetaTable

/**
 * Created by journey on 14-12-11.
 */
@Deprecated // todo ?
class TableRegister {
    private static Map<String, MetaTable> tableMapping = [:]

    static Map getTableMapping() {
        tableMapping
    }

    static Map register(String key, MetaTable table) {
        tableMapping.put(key, table)
    }

    static MetaTable getModel(String key) {
        tableMapping.get(key)
    }

    static def readTableFromFile(File file) {
        new JsonSlurper().parseText(file.text)
    }

    static void loadTables() {
        def dir = '/home/journey/day/svn/cbt/src/main/resources/config/tables'
        new File(dir).listFiles().each { File f ->
            def json = readTableFromFile(f)
            def fields = []
            json.fields.each { k, v ->
                fields.add(new Field(
                        columnName: k,
                        caption: v.caption,
                        type: FieldType.valueOf(v.type)
                ))
            }
            MetaTable meta = new MetaTable(
                    key: json.key,
                    tableName: json.tableName,
                    prefix: json.prefix ?: '',
                    idColumnName: json.idColumnName ?: 'id',
                    headerIdColumnName: json.headerIdColumnName ?: 'header_id',
                    headerNoColumnName: json.headerNoColumnName ?: 'bill_no',
                    idDetail: json.containsKey('isDetail') ? json.isDetail : false,
                    fields: fields
            )
            register(meta.key, meta)
        }
    }


}
