package ttx.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
class RedisUtil {
    private final static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    static String get(String key) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.get(key)
        }
    }

    static Long getLong(String key) {
        pool.getResource().withCloseable { Jedis jedis ->
            new Long(jedis.get(key))
        }
    }
//        try (Jedis jedis = pool.getResource()) {
//            /// ... do stuff here ... for example
//            jedis.set("foo", "bar");
//            String foobar = jedis.get("foo");
//            jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike");
//            Set<String> sose = jedis.zrange("sose", 0, -1);
//        }

    static void set(String key, String value) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.set(key, value)
        }
    }

    static long incr(String key) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.incr(key)
        }
    }

    static long incrBy(String key, long integer) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.incrBy(key, integer)
        }
    }

    static String hget(String key, String fieldKey) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.hget(key, fieldKey)
        }
    }

    static void hset(String key, String fieldKey, String value) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.hset(key, fieldKey, value)
        }
    }

    static Jedis jedis() {
        pool.getResource()
    }

}
