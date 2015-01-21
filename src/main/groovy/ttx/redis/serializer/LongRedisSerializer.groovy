package ttx.redis.serializer

import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
@Deprecated // todo ?
class LongRedisSerializer implements RedisSerializer<Long> {
    @Override
    byte[] serialize(Long aLong) throws SerializationException {
        if (null != aLong) {
            aLong.toString().getBytes()
        } else {
            new byte[0]
        }
    }

    @Override
    Long deserialize(byte[] bytes) throws SerializationException {
        if (bytes.length > 0) {
            Long.parseLong(new String(bytes))
        } else {
            0
        }
    }
}
