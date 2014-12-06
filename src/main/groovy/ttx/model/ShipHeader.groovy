package ttx.model

import ttx.model.base.BaseModel

/**
 * Created by journey on 14-12-3.
 */
class ShipHeader extends BaseModel {
    String headerTableName = 'wm_ship_header'
    String lineTableName = 'wm_ship_line'

    String owner
    int count

    ShipHeader() {
        super()
        tableFieldNameMapping.putAll([
                'sh_owner': 'Owner',
                'sh_count': 'Count'
        ])
    }
}
