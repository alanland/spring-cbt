package ttx.web.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ttx.service.AuthService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
class AuthInterceptor extends HandlerInterceptorAdapter {
    // TODO
    AuthService service = new AuthService()

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String user = request.getHeader("X-User") // todo 去掉硬编码
        String token = request.getHeader("X-Token")

        if (user && token &&
                (AuthService.checkLogin(user, token) ||
                        (user == 'admin' && token == 'pass')
                )
        ) {
            return true
        } else {
            // todo 加入处理信息，用户controller判断
            response.sendRedirect('/rest/redirect/error')
            return false
        }
    }
}
