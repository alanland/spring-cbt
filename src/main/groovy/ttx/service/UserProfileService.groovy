package ttx.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ttx.service.base.BaseService
import ttx.util.Coder

/**
 * ＠author 王成义 
 * @created 2015-01-12.
 */
@Configuration
class UserProfileService extends BaseService {
    @Bean
    UserProfileService userService() {
        new UserProfileService()
    }

    void beforeSave(user) {
        user.password = Coder.encodedPassword(user.password.getBytes())
    }
}
