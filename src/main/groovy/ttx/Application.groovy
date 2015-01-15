package ttx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import ttx.web.listener.StartupCacheInitializer

@ComponentScan
@EnableAutoConfiguration
class Application {
    static void main(String[] args) {
//        JdbcUtil.initData()
//        UserDatas.create()
        SpringApplication.run(Application.class, args).
                addApplicationListener(new StartupCacheInitializer())

    }
}
