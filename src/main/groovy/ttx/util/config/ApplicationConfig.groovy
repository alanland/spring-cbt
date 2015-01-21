package ttx.util.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * ＠author 王成义 
 * @created 2015-01-21.
 */
@ConfigurationProperties
@Configuration
class ApplicationConfig {
    Map datasource

    @Bean
    ApplicationConfig config() {
        new ApplicationConfig()
    }

}