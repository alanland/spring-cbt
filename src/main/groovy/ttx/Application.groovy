package ttx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import ttx.jdbc.JdbcUtil
import ttx.web.listener.StartupInitializer

@ComponentScan
@EnableAutoConfiguration
class Application {
    static void main(String[] args) {
        def ctx = SpringApplication.run(Application.class, args)
        ctx.addApplicationListener(new StartupInitializer())
    }
}
