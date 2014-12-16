package ttx.service

@Deprecated
class RegistryCenter {
    private static List<Map> billMapping = [
//            [id: 'pick', name: 'Pick', header: ShipHeader, line: ShipLine],
//            [id: 'ship', name: 'Ship']
    ]

    static void register(String key, String name) {
        billMapping.add([id: key, name: name])
    }

    static def getMapping() {
        billMapping.clone()
    }

    static Map getMappingItem(String id) {
        for (Map item : billMapping) {
            if (item['id'].toString().equalsIgnoreCase(id)) return item
        }
        null
    }
}
