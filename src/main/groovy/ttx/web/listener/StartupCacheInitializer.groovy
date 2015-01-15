package ttx.web.listener

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import ttx.service.base.CreationService
import ttx.util.SequenceGenerator

/**
 * ＠author 王成义 
 * @created 2015-01-14.
 */
@Component
class StartupCacheInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    CreationService creationService

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SequenceGenerator.syncFromDb()
        creationService.buildCache()
    }
}
