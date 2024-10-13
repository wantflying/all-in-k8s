package org.yyy.homek8s.natterexportconfig.webconfig;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yyy.homek8s.natterexportconfig.utils.MD5Util;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String SECRET_TOKEN = "b840fdcc206ddbf7d83f2945c125c3c8"; // 替换为你的实际 token

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        if (token == null || !SECRET_TOKEN.equals(MD5Util.crypt(token))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return false; // 拦截请求
        }

        return true; // 放行请求
    }
}