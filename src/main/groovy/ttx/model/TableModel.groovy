package ttx.model

import org.codehaus.jackson.annotate.JsonIgnore


/**
 * Created by journey on 14-12-11.
 */
class TableModel {
    @JsonIgnore
    MetaTable metaTable
    Long id
    String billNo
    Long headerId
    @JsonIgnore
    Map<String, Object> valueMap

    TableModel() {}

    private TableModel(String key) {

    }

    Map<String, Object> syncFromModel() {
// TODO
    }

    TableModel syncFromMap() {
// TODO
    }
}
