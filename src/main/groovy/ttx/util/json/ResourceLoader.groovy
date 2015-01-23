package ttx.util.json

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.WebApplicationContext

/**
 * Created by journey on 14-12-11.
 */
@Configuration
class ResourceLoader {
    @Autowired
    private WebApplicationContext ctx

    @Bean
    ResourceLoader resourceLoader() {
        new ResourceLoader()
    }

    def getJsonFromResource(String file) {
        new JsonSlurper().parse(ctx.getResource("classpath:${file}.json").getFile())
    }

    String getJsonStringFromResource(String file) {
        JsonOutput.toJson(new JsonSlurper().parse(ctx.getResource("classpath:${file}.json").getFile()))
    }

    def getJsonFromFile(File file) {
        new JsonSlurper().parse(file)
    }

    String getJsonStringFromFile(File file) {
        JsonOutput.toJson(getJsonFromFile(file))
    }
}
