package ttx.web.listener

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
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
    JdbcUtil jdbc
    @Autowired
    ApplicationConfig config
    @Autowired
    SequenceGenerator generator

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 初始数据
        initData()
        // 单号同步到缓存

        generator.syncFromAllDb()
        // 单据对象缓存
//        creationService.buildCache()
    }

    def initData() {
        jdbc.templates.keySet().each { initDataDb(it) }
    }

    def initDataDb(String db) {
        int count = jdbc.template(db).queryForObject("select count(1) from ttx_system where key=?", Integer.class, 'data_updated')
        if (count) {
            return
        }
        jdbc.insert(db).withTableName('ttx_navigator').execute([
                version  : 0,
                key      : 'admin',
                structure: jsonString('ttx/config/navigator')
        ])
        ctx.getResource("classpath:ttx/config/table").getFile().listFiles().each { file ->
            def jsonObject = json(file)
            jdbc.insert(db).withTableName('ttx_table_model').execute([
                    version  : 0,
                    key      : jsonObject.key,
                    structure: jsonString(file)
            ])
        }
        ctx.getResource("classpath:ttx/config/bill").getFile().listFiles().each { file ->
            def jsonObject = json(file)
            jdbc.insert(db).withTableName('ttx_bill_model').execute([
                    version  : 0,
                    key      : jsonObject.key,
                    structure: jsonString(file)
            ])
        }
        ctx.getResource("classpath:ttx/config/view").getFile().listFiles().each { file ->
            def jsonObject = json(file)
            jdbc.insert(db).withTableName('ttx_wso_data').execute([
                    version  : 0,
                    tid      : jsonObject.tid,
                    oid      : jsonObject.oid,
                    structure: jsonString(file)
            ])
        }
        jdbc.insert(db).withTableName('ttx_system').execute([
                key  : 'data_updated',
                value: '1'
        ])
    }

    String jsonString(File file) {
        resourceLoader.getJsonStringFromFile(file)
    }

    String jsonString(String file) {
        resourceLoader.getJsonStringFromResource(file)
    }

    def json(File file) {
        resourceLoader.getJsonFromFile(file)
    }
}
