package ttx.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.WebApplicationContext
import ttx.jdbc.JdbcUtil
import ttx.service.base.BaseService
import ttx.service.base.CreationService
import ttx.util.ModelCache
import ttx.util.SequenceGenerator

/**
 * ＠author 王成义 
 * @created 2015-01-13.
 */
@Configuration
class RestTableService extends BaseService {

    @Bean
    RestTableService restTableService() {
        new RestTableService()
    }

    def create(WebApplicationContext ctx, String tableKey, Map map) {
        Map tableModel = ModelCache.getCachedModel(ModelCache.TABLE_TABLE_MODEL, tableKey)
        // TODO 独立成单独的模块
        if (tableModel.autoNoExpression && !map[tableModel.autoNoExpression]) {
            def no = tableModel.autoNoExpression.replaceAll(/\{.*?\}/) { String m ->
                String exp = m.substring(1, m.length() - 1)
                if (exp.matches(/\d*/)) {
                    String.format("%0${exp.length()}d", SequenceGenerator.next())
                } else {
                    new Date().format(exp)
                }
            }
            tableModel.fields.each {
                if (it.autoNo && it.autoNo == '1') {
                    map[it.field] = no
                }
            }
        }
        map[tableModel.idColumnName] = SequenceGenerator.next()
        if (tableModel.service){
            ctx.getBean(tableModel.service+'Service').beforeSave(map)
        }
        JdbcUtil.getInsert().withTableName(tableModel.tableName).execute(map)
    }
}
