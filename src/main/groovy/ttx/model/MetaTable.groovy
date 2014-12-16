package ttx.model

import groovy.json.JsonBuilder
import org.codehaus.jackson.annotate.JsonIgnore

/**
 * Created by journey on 14-12-11.
 */
class MetaTable {
    String key
    String tableName
    String prefix = ''
    @JsonIgnore
    Class modelClass
    String idColumnName = 'id'
    Boolean idDetail = false
    String headerIdColumnName = 'header_id'
    String headerNoColumnName = 'bill_no'
    List<Field> fields;

    JsonBuilder getJsonBuilder() {
        def json = new JsonBuilder()
        json {
            'key' key
            'tableName' tableName
            'prefix' prefix
            'idColumnName' idColumnName
            'headerIdColumnName' headerIdColumnName
            'headerNoColumnName' headerNoColumnName
            'fields' fields
        }
        json
    }
}
