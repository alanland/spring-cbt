package ttx.service

import groovy.json.JsonSlurper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ttx.redis.RedisUtil
import ttx.service.base.BaseService
import ttx.util.Coder
import ttx.util.config.AppProfile

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
@Configuration
class AuthService extends BaseService {

    // todo 这些变量应该放在哪里
    private static final String KEY_LOGIN = "login"
    private static final String HKEY_TOKEN = "token"
    private static final String HKEY_LAST_ACTIVE = "lastActive"
    private static final String MODEL_TABLE_USER = 'user'

    @Bean
    AuthService authService() {
        new AuthService()
    }

    // 0 success
    // 1 error
    // 2 no user
    // 3 invalid password
    String doLogin(String db, String username, String password) {
        if (checkPassword(db, username, password)) {
            String token = generateToken(db, username)
            RedisUtil.hset("$db:$KEY_LOGIN:$username", HKEY_TOKEN, token)
            RedisUtil.hset("$db:$KEY_LOGIN:$username", HKEY_LAST_ACTIVE, new Date().getTime().toString())
            return token
        } else {
            return null
        }
    }

    String generateToken(String db, String username) {
        String token = RedisUtil.hget("$db:$KEY_LOGIN:$username", HKEY_TOKEN)
        if (token) {
            return token
        } else {
            return UUID.randomUUID().toString().replaceAll('-', '')
        }
    }

    private boolean checkPassword(String db, String username, String password) {
        password = Coder.encodedPassword(password.getBytes())
        def userTableModel = new JsonSlurper().parseText(RedisUtil.get("$db:${AppProfile.TABLE_TABLE_MODEL}:$MODEL_TABLE_USER"))
        1 == template(db).queryForObject("select count(1) from ${userTableModel.tableName} where code=? and password=?", Integer.class, username, password)
    }

    static boolean checkLogin(String db, String username, String token) {
        if (RedisUtil.hget("$db:$KEY_LOGIN:$username", HKEY_TOKEN) == token) {
            // 判断超时的事情统一由心跳作业来处理
            RedisUtil.hset("$db:$KEY_LOGIN:$username", HKEY_LAST_ACTIVE, new Date().getTime().toString())
            return true
        } else {
            return false
        }
    }
}
