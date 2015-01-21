package ttx.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ttx.jdbc.JdbcUtil
import ttx.redis.RedisUtil

/**
 * ＠author 王成义 
 * @created 2015-01-03.
 */
@Configuration
class SequenceGenerator {

    @Autowired
    JdbcUtil jdbc

    private static final TABLE = 'ttx_sequence'

    SequenceGenerator() {}

    @Bean
    SequenceGenerator sequenceGenerator() {
        new SequenceGenerator()
    }

    long next(String db, String key = '') {
        long s = RedisUtil.getLong(key)
        def template = jdbc.template(db)
        int count = template.queryForObject("select count(1) from ${TABLE}", Integer.class)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", key, s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+1 where key=?", key)
        }
        s
    }

    def next(String db, String key = '', long delta) {
        long current = RedisUtil.getLong(key)
        long s = RedisUtil.incrBy(key, delta)
        def template = jdbc.template(db)
        int count = template.queryForObject("select count(1) from ${TABLE} where key=?", Integer.class, key)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", key, s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+${delta} where key=?", key)
        }
        (current + 1)..s
    }

    void syncFromAllDb() {
        jdbc.templates.keySet().each { String db ->
            syncFromDb(db)
        }
    }

    void syncFromDb(String db) {
        try {
            List l = jdbc.template(db).queryForList("select key,sequence from ${TABLE}")
            l.each { item ->
                RedisUtil.set(item.key as String, item.sequence as String)
            }
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }
}
