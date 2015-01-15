package hello.rest_service

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * ＠author 王成义 
 * @created 2015-01-15.
 */
@Configuration
@EnableWebMvc
class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns('/cn/**')
    }
}
