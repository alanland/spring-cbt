package ttx.util

import ttx.jdbc.JdbcUtil
import ttx.redis.RedisUtil

import java.util.concurrent.atomic.AtomicLong

/**
 * ＠author 王成义 
 * @created 2015-01-03.
 */
class SequenceGenerator {
    private static final TABLE = 'ttx_sequence'

    private SequenceGenerator() {}

    static final AtomicLong sequence = new AtomicLong()

    static long next(String key = '') {
        long s = RedisUtil.getLong(key)
        def template = JdbcUtil.getTemplate()
        int count = template.queryForObject("select count(1) from ${TABLE}", Integer.class)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", key, s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+1 where key=?", key)
        }
        s
    }

    static def next(String key = '', long delta) {
        long current = RedisUtil.getLong(key)
        long s = RedisUtil.incrBy(key, delta)
        def template = JdbcUtil.getTemplate()
        int count = template.queryForObject("select count(1) from ${TABLE} where key=?", Integer.class, key)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", key, s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+${delta} where key=?", key)
        }
        (current + 1)..s
    }

    static def syncFromDb() {
        try {
            List l = JdbcUtil.getTemplate().queryForList("select key,sequence from ${TABLE}")
            l.each { item ->
                RedisUtil.set(item.key as String, item.sequence as String)
            }
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }
}
