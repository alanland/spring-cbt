package hello.rest_service

import org.omg.IOP.ServiceContextListHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.support.RequestContextUtils

import javax.servlet.http.HttpServletRequest
import java.util.concurrent.atomic.AtomicLong

@RestController
public class GreetingController {

    @Autowired
    MyService service

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping('/login')
    Object login(){
        'login'
    }
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        service.service()
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping('/ctx')
    Object ctx(HttpServletRequest request) {
        WebApplicationContext content = RequestContextUtils.getWebApplicationContext(request);
        MyService myService = content.getBean('myService',MyService.class)
        myService.service()
//        [status: 'ok']
        Map map = new HashMap()
        map.put('status','ok')
        return map

//        content = request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }
}