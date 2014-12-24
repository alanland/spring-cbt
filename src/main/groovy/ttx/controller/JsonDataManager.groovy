package ttx.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ttx.util.json.ResourceLoader

/**
 * 获取一些客户端界面元素的数据，比如菜单，导航，单据结构
 */
@RestController
@RequestMapping('/rest/data')
class JsonDataManager {
    @RequestMapping('navigator')
    def navigator() {
        ResourceLoader.getJsonStringByFile('navigator')
    }

    @RequestMapping('menu')
    def menu() {
        ResourceLoader.getJsonStringByFile('menu')
    }

    @RequestMapping('wsoDefinition/{tid}')
    def wsoDefinition(@PathVariable String tid) {
        tid // todo
    }

    @RequestMapping('billDefinition/{tid}')
    def billDefinition(@PathVariable String tid) {
        ResourceLoader.getJsonStringByFile("view/$tid")
    }
}
