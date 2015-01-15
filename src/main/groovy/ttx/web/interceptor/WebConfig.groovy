package ttx.web.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * ＠author 王成义 
 * @created 2015-01-09.
 */
@Configuration
@EnableWebMvc
class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns(
                '/rest/domain/**',
                '/rest/creation/navigator'
        )
//                .excludePathPatterns(
//                '/error',
//                '/rest/redirect/**',
//                '/rest/creation/**',
//                '/rest/jf/**'
//        )
        // todo  error page
//        registry.addInterceptor(new LocaleInterceptor());
//        registry.addInterceptor(new ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
//        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
    }
}
