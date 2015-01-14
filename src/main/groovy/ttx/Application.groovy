package ttx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import ttx.service.base.CreationService
import ttx.util.SequenceGenerator

@ComponentScan
@EnableAutoConfiguration
class Application {
    static void main(args) {
//        JdbcUtil.initData()
//        UserDatas.create()
        SequenceGenerator.syncFromDb()
        new CreationService().buildCache()
        SpringApplication.run(Application.class, args)
    }
}
