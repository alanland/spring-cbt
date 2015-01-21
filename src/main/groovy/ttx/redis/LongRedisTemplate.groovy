package ttx.redis

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import ttx.redis.serializer.LongRedisSerializer

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
@Deprecated // todo ?
class LongRedisTemplate extends RedisTemplate<String,Long>{
    LongRedisTemplate(){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Long> longSerializer = new LongRedisSerializer()
        setKeySerializer(stringSerializer)
        setValueSerializer(longSerializer)
        setHashKeySerializer(stringSerializer)
        setHashValueSerializer(longSerializer)
    }

    LongRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        this()
        setConnectionFactory(jedisConnectionFactory)
        afterPropertiesSet()
    }
}
