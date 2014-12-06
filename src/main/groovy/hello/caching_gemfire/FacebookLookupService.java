package hello.caching_gemfire;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;

public class FacebookLookupService {

    RestTemplate restTemplate = new RestTemplate();

    @Cacheable("hello")
    public Page findPage(String page) {
//        return restTemplate.getForObject("http://graph.facebook.com/" + page, Page.class);
        Page p = new Page();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        p.setName(page);
        p.setWebsite(page+"  xxxx website");
        return p;
    }

}