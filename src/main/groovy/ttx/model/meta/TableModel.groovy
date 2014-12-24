package ttx.model.meta

import org.codehaus.jackson.annotate.JsonIgnore

/**
 * Created by journey on 14-12-11.
 */
class TableModel {
    @JsonIgnore
    MetaTable metaTable
    @JsonIgnore
    Map<String, Object> valueMap

    TableModel() {}

    protected TableModel(String key) {

    }

    Map<String, Object> syncFromModel() {
// TODO
    }

    TableModel syncFromMap() {
// TODO
    }
}
