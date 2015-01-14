package redis

import redis.clients.jedis.Jedis
import ttx.redis.RedisUtil

/**
 * ＠author 王成义 
 * @created 2015-01-12.
 */
class JedisTest {
    static void main(args){
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        String value = jedis.get("foo1");
//        println jedis.hget('test','xx')
        println value
        println RedisUtil.get('foo')
    }
}
