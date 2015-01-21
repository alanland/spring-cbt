package ttx.web.listener

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import ttx.jdbc.JdbcUtil
import ttx.service.base.CreationService
import ttx.util.SequenceGenerator
import ttx.util.config.ApplicationConfig
import ttx.util.json.ResourceLoader

/**
 * ＠author 王成义 
 * @created 2015-01-14.
 */
@Component
class StartupInitializer implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    CreationService creationService
    @Autowired
    ResourceLoader resourceLoader
    @Autowired
    ApplicationContext ctx
    @Autowired
    JdbcUtil jdbcUtil
    @Autowired
    ApplicationConfig config
    @Autowired
    SequenceGenerator generator

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 初始数据
//        initData()
        // 单号同步到缓存

        generator.syncFromAllDb()
        // 单据对象缓存
//        creationService.buildCache()
    }

    def initData() {
        int count = JdbcUtil.getTemplate().queryForObject("select count(1) from ttx_system where key=?", Integer.class, 'data_updated')
        if (count) {
            return
        }
        SimpleJdbcInsert insert = JdbcUtil.getInsert()
        insert.withTableName('ttx_navigator').execute([
                version  : 0,
                key      : 'admin',
                structure: jsonString('ttx/config/navigator')
        ])
        insert.withTableName('ttx_wso_data').execute([
                version  : 0,
                tid      : 'ttx/dijit/wso/Creation',
                nid      : '*',
                structure: jsonString('ttx/config/view/Creation')
        ])
        insert.withTableName('ttx_wso_data').execute([
                version  : 0,
                tid      : 'ttx/dijit/wso/Bill',
                nid      : 'BillTemplate',
                structure: jsonString('ttx/config/view/BillTemplate')
        ])
        insert.withTableName('ttx_system').execute([
                key  : 'data_updated',
                value: '1'
        ])
    }

    String jsonString(String file) {
        JsonOutput.toJson(resourceLoader.getJsonStringFromResource(file))
    }
}
