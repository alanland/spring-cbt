package ttx.service

import ttx.model.ShipHeader
import ttx.model.ShipLine
import ttx.service.base.CreationService

/**
 * Created by journey on 14-12-3.
 */
class ShipService extends CreationService {
    ShipService() {
        super()
        header = new ShipHeader()
        line = new ShipLine()
    }
}
