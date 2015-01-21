package ttx.store

/**
 * ＠author 王成义 
 * @created 2015-01-19.
 */
class Store {
    List<Map<String, Object>> data
    String idAttr

    Object get(String id) {
        def res = data.find {
            it.get(idAttr) == id
        }
        res
    }
    


}
