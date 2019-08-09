package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 使用拦截器可以在请求执行的不同阶段，批量的处理请求
 * 在WebMVCConfig当中进行配置
 * 然后在模板上进行处理
 */

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 请求传入Controller之前
     * 在请求开始之前，通过cookie找到凭证，通过ticket找到user对象，把user暂存到TreadLocal当中
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        //判断ticket是否存在
        if (ticket != null) {
            // 查询凭证，如果ticket存在调用userService把整个loginTicket对象查到
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());

                /**
                 *难点
                 * 在本次请求中持有用户
                 *每个浏览器访问服务器，服务器会开多个线程来处理不同的请求（多对一，并发环境）
                 * 在存用户的时候要考虑多线程，如果只是简单的当成一个变量存到一个容器当中，可能会产生冲突，在并发的情况下
                 * 把数据存到一个地方，并且能够工作在并发的情况下，就必须要考虑线程的隔离，每个线程单独存一份对象，让他们不互相干扰，->TreadLocal
                 * TreadLocal在多个线程之间隔离存对象
                 */
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    /**
     * 在模板引擎被调用之前，把user存到modelAndView当中
     * 从 hostHolder里面得到当前线程持有的user
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 在整个请求执行完以后，调用hostHolder.clear()把数据清理掉
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
