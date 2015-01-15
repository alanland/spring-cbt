package hello.rest_service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ＠author 王成义
 *
 * @created 2015-01-15.
 */
@RestController
@RequestMapping("/cn")
public class CnController extends IController {
    @RequestMapping("1")
    public Object m1() {
        Map map = new HashMap();
        map.put(1, 1);

        return map;
    }
}
