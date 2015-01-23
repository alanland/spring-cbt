package ttx.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.stereotype.Component
import paillard.florent.springframework.simplejdbcupdate.SimpleJdbcUpdate
import ttx.util.config.ApplicationConfig

import javax.annotation.PostConstruct

@Component
@Configuration
class JdbcUtil {

    @Bean
    JdbcUtil jdbcUtil() {
        new JdbcUtil()
    }

    @Autowired
    ApplicationConfig config

    @Autowired
    ApplicationContext ctx

    // 认为不可变，用hashmap即可
    Map<String, SimpleDriverDataSource> dataSources = [:]
    Map<String, JdbcTemplate> templates = [:]
    Map<String, NamedParameterJdbcTemplate> namedTemplates = [:]
    Map<String, SimpleJdbcInsert> inserts = [:]
    Map<String, SimpleJdbcUpdate> updates = [:]

    JdbcUtil() {}

    @PostConstruct
    // todo for production
    void initIt() throws Exception {
        config.datasource.each { String k, v ->
            SimpleDriverDataSource s = new SimpleDriverDataSource([
                    driverClass: Class.forName(v['driver-class-name']),
                    username   : v.username,
                    password   : v.password,
                    url        : v.url
            ])
            dataSources.put(k, s)
            templates.put(k, new JdbcTemplate(s))
            namedTemplates.put(k, new NamedParameterJdbcTemplate(s))
            inserts.put(k, new SimpleJdbcInsert(s))
            updates.put(k, new SimpleJdbcUpdate(s))
        }
    }

    JdbcTemplate template(String db) {
        templates.get(db)
    }

    NamedParameterJdbcTemplate namedTemplate(String db) {
        namedTemplates.get(db)
    }

    SimpleJdbcInsert insert(String db) {
//        inserts.get(db)
        new SimpleJdbcInsert(dataSources.get(db))
    }

    SimpleJdbcUpdate update(String db) {
//        updates.get(db)
        new SimpleJdbcUpdate(dataSources.get(db))
    }
}
