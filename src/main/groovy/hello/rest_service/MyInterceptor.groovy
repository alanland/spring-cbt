package hello.rest_service

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * ＠author 王成义 
 * @created 2015-01-15.
 */
class MyInterceptor extends HandlerInterceptorAdapter {
    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURL().contains('hello')) {
            return true
        } else {
            response.sendRedirect("/login")
            return false
        }
    }
}
