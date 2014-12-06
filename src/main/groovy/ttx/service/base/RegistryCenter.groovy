package ttx.service.base

/**
 * Created by journey on 14-12-5.
 */
class RegistryCenter {
    private static billMapping = [
            [id:'pick',name:'Pick'],
            [id:'ship',name:'Ship']
    ]

    static void register(String key, String name) {
        billMapping.put(key, name)
    }

    static def getMapping() {
        billMapping.clone()
    }

}
