package ttx.util

/**
 * ＠author 王成义 
 * @created 2015-01-13.
 */
class ModelCache {

    static final TABLE_TABLE_MODEL = "ttx_table_model"
    static final TABLE_BILL_MODEL = "ttx_bill_model"
    static final TABLE_VIEW_MODEL = "ttx_view_model"

    static final CACHE_KEYS = [
            TABLE_TABLE_MODEL,
            TABLE_BILL_MODEL,
            TABLE_VIEW_MODEL
    ]

    final static Map cache = new Hashtable()

    def static getCachedModel(String table, String key) {
        cache[table].find {
            it.key == key
        }
    }

    def static clear() {
        cache.clear()
    }

    def static tableCache(){
        cache[TABLE_TABLE_MODEL]
    }
    def static billCache(){
        cache[TABLE_BILL_MODEL]
    }
    def static viewCache(){
        cache[TABLE_VIEW_MODEL]
    }

}
