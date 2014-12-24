package ttx.model.meta

import groovy.json.JsonBuilder
import org.codehaus.jackson.annotate.JsonIgnore

/**
 * @author 王成义
 * @created 14-12-11.
 */
class MetaTable {

    static final VERSION = '1.0'

    String key
    String tableName
    String prefix = ''
    @JsonIgnore
    // todo to delete
    Class modelClass
    String idColumnName = 'id'
    List<Field> fields;

    JsonBuilder getJsonBuilder() {
        def json = new JsonBuilder()
        json {
            'key' key
            'tableName' tableName
            'prefix' prefix
            'idColumnName' idColumnName
            'fields' fields
        }
        json
    }
}
