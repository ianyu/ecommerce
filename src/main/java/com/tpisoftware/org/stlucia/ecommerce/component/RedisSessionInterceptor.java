package com.tpisoftware.org.stlucia.ecommerce.component;

import com.tpisoftware.org.stlucia.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RedisSessionInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            try {
                User user = (User) session.getAttribute("user");
                //验证当前请求的session是否是已登录的session
                String loginSessionId = redisTemplate.opsForValue().get("loginUser:" + user.getName());
                if (loginSessionId != null && loginSessionId.equals(session.getId())) {
                    // 未登入，返回登入頁面
                    request.setAttribute("msg", "沒有權限，請先登入");
                    request.getRequestDispatcher("/index.html").forward(request, response);
                }
            } catch (Exception e) {
                request.setAttribute("msg", "沒有權限，請先登入");
                request.getRequestDispatcher("/index.html").forward(request, response);

            }
        }
        response401(response);
        return false;
    }

    private void response401(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            //response.sendRedirect("/index.html").\;
           // response.setContentType();
           // response.setAttribute("msg", "沒有權限，請先登入");
            //response.getRequestDispatcher("/index.html").forward(request, response);
            response.getWriter().print("用户未登录或登陆超时！");
           //response.getWriter().print("用户未登录或登陆超时！");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}