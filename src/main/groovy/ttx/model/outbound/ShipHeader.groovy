package ttx.model.outbound

import ttx.model.TableModel

/**
 * Created by journey on 14-12-3.
 */
class ShipHeader extends TableModel {
    String headerTableName = 'wm_ship_header'
    String lineTableName = 'wm_ship_line'

    String owner
    int count

    ShipHeader() {
        super()
//        tableFieldNameMapping.putAll([
//                'sh_owner': 'Owner',
//                'sh_count': 'Count'
//        ])
    }
}
