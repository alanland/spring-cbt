package ttx.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.WebApplicationContext
import ttx.service.base.BaseService
import ttx.util.ModelCache
import ttx.util.SequenceGenerator

/**
 * ＠author 王成义 
 * @created 2015-01-13.
 */
@Configuration
class RestTableService extends BaseService {
    @Autowired
    WebApplicationContext ctx
    @Autowired
    SequenceGenerator generator

    @Bean
    RestTableService restTableService() {
        new RestTableService()
    }

    def create(String db, String tableKey, Map map) {
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        // TODO 独立成单独的模块 U{yyy-MM-dd}{000000}
        tableModel.fields.each {
            if (it.autoNo) {
                String p = it.autoNo
                if (p == 0 || p == '0') {
                    map[it.field] = generator.next(db, tableModel.tableName)
                } else {
                    map[it.field] = p.replaceAll(/\{.*?\}/) { String m ->
                        String exp = m.substring(1, m.length() - 1)
                        if (exp.matches(/\d*/)) {
                            String.format("%0${exp.length()}d", generator.next(db, tableModel.tableName))
                        } else {
                            new Date().format(exp)
                        }
                    }
                }
            }
        }
        if (tableModel.service) {
            ctx.getBean(tableModel.service + 'Service').beforeSave(map)
        }
        insert(db).withTableName(tableModel.tableName).execute(map)
    }

    def saveOrUpdate(String db, String tableKey, Map map) {
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        def id = map[tableModel.idColumnName]
        int count = template(db).queryForObject(
                "select count(1) from ${tableModel.tableName} where ${tableModel.idColumnName}=?",
                Integer.class, id
        )
        if (count != 1) {
            return [code: 1, desc: "$count items exist in ${tableModel.tableName}"]
        }
        def where = []
        where[tableModel.idColumnName] = map.get(tableModel.idColumnName)
        map.remove(tableModel.idColumnName)
        update(db).withTableName(tableModel.tableName).execute(map, where)
        [code: 0]
    }

    def get(String db, String tableKey, String idStr) {
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        def item = tableModel.fields.find {
            it.field == tableModel.idColumnName
        }
        def id = item.type == 'integer' ? Long.valueOf(idStr) : idStr
        template(db).queryForMap("select * from ${tableModel.tableName} where ${tableModel.idColumnName}=?", id)
    }

    def filterSelect(String db, String tableKey) {
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        String tableName = tableModel.tableName
        String idAttr = tableModel.idAttr ?: tableModel.idColumnName
        String labelAttr = tableModel.labelAttr ?: tableModel.idColumnName
        // todo size == 0 try catch
//        def rtn = []
//        try {
//            rtn = service.getTemplate().queryForMap("select $idColumnName id, code from ${tableName} ")
//        } catch (EmptyResultDataAccessException e) {
//        } finally {
//            return rtn;
//        }
        template(db).queryForList("select $idAttr as id, $labelAttr as label from ${tableName} ")
    }

    def deleteTable(String db, String tableKey, Map map) {
        List list = map.items
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        String idColumn = tableModel.idColumnName
        def item = tableModel.fields.find {
            it.field == idColumn
        }
        if (item.type == 'string')
            list = list.collect {
                "'$it'"
            }
        String sql = "delete from ${tableModel.tableName} where ${idColumn} in(${list.join(',')})"
        template(db).update(sql)
        [code: 0]
    }
}
