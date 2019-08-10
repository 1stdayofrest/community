package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 写完拦截器之后，还需要配置它。-》把静态资源排除掉
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //强制类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取对应方法
            Method method = handlerMethod.getMethod();
            //获取方法上面的注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //该方法被loginRequired标记，并且用户没有登录
            if ((loginRequired != null && hostHolder.getUser() == null)) {
                //重定向到登录界面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }

        }
        return true;
    }
}
