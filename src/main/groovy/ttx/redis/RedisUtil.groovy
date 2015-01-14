package ttx.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
class RedisUtil {
//    private final static JedisConnectionFactory connectionFactory =
//            new JedisConnectionFactory(hostName: 'localhost', port: 6379) // 3379
//    private final static StringRedisTemplate stringTemplate = new StringRedisTemplate(connectionFactory)
//    private final static LongRedisTemplate longTemplate = new LongRedisTemplate(connectionFactory);
    private final static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

//    private final static Jedis jedis = new Jedis('localhost')

    static String get(String key) {
        pool.getResource().withCloseable { Jedis jedis ->
            jedis.get(key)
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

//
//    static StringRedisTemplate getStringTemplate() {
//        return stringTemplate
//    }
//
//    static LongRedisTemplate getLongTemplate() {
//        return longTemplate
//    }
//
//    static String getString(String key) {
//        stringTemplate.opsForValue().get(key)
//    }
//
//    static void setString(String key, String value) {
//        stringTemplate.opsForValue().set(key, value)
//    }
//
//    static String getAndSetString(String key, String value) {
//        stringTemplate.opsForValue().getAndSet(key, value)
//    }
//
//    static String getLong(String key) {
//        longTemplate.opsForValue().get(key)
//    }
//
//    static void setLong(String key, Long value) {
//        longTemplate.opsForValue().set(key, value)
//    }
//
//    static String increment(String key, Long delta) {
//        longTemplate.opsForValue().increment(key, delta)
//    }
//
//    static String hGetString(String key, String hashKey) {
//        stringTemplate.opsForHash().get(key, hashKey)
//    }
//
//    static void hSetString(String key, String hashKey, String value) {
//        stringTemplate.opsForHash().put(key, hashKey, value)
//    }
//
//    static Long hGetLong(String key, String hashKey) {
//        longTemplate.opsForHash().get(key, hashKey)
//    }
//
//    static void hSetLong(String key, String hashKey, Long value) {
//        longTemplate.opsForHash().put(key, hashKey, value)
//    }


}
