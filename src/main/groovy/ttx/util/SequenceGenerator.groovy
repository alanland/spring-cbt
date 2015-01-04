package ttx.util

import ttx.jdbc.JdbcUtil

import java.util.concurrent.atomic.AtomicLong

/**
 * ＠author 王成义 
 * @created 2015-01-03.
 */
class SequenceGenerator {
    private static final TABLE = 'ttx_sequence'

    private SequenceGenerator() {}

    static final AtomicLong sequence = new AtomicLong()

    static long next() {
        long s = sequence.incrementAndGet();
        def template = JdbcUtil.getTemplate()
        int count = template.queryForObject("select count(1) from ${TABLE}", Integer.class)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", 'global', s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+1")
        }
        s
    }

    static def next(long delta) {
        long current = sequence.get()
        long s = sequence.getAndAdd(delta)
        def template = JdbcUtil.getTemplate()
        int count = template.queryForObject("select count(1) from ${TABLE}", Integer.class)
        if (count == 0) {
            template.update("insert into ${TABLE}(key,sequence) values(?,?) ", 'global', s)
        } else {
            template.update("update ${TABLE} set sequence=sequence+${s}")
        }
        (current + 1)..s
    }

    static def syncFromDb() {
        try {
            long l = JdbcUtil.getTemplate().queryForObject("select sequence from ${TABLE} where key=?", Integer.class, 'global')
            sequence.set(l)
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }
}
