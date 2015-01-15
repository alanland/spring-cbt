package hello.rest_service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * ＠author 王成义
 * @created 2015-01-15.
 */
@Configuration
class MyConf {
    @Bean
    MyService myService(){
        return new MyService()
    }
}
