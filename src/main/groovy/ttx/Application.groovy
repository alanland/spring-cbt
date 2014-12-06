package ttx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import ttx.jdbc.JdbcUtil

@ComponentScan
@EnableAutoConfiguration
class Application {
    static void main(args){
//        JdbcUtil.initData()
        SpringApplication.run(Application.class, args)
    }
}
