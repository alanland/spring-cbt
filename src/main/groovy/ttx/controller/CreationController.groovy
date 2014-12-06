package ttx.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ttx.service.base.BaseService
import ttx.service.base.RegistryCenter

/**
 * Created by journey on 14-12-5.
 */
@RestController
@RequestMapping('/rest/creation')
class CreationController {
    private final BaseService service = new BaseService()

    @RequestMapping('billMapping')
    def billMapping() {
        RegistryCenter.getMapping()
    }
}
