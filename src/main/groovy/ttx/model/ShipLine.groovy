package ttx.model

class ShipLine extends ShipHeader {

    String material
    String spec
    int qty

    ShipLine() {
        super()
        tableFieldNameMapping.putAll([
                'sl_material': 'Material',
                'sl_spec'    : 'Spec',
                'sl_qty'     : 'Qty'
        ])
    }
}
