package com.study.shopping.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.study.shopping.pojo.Result;
import com.study.shopping.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        String url=req.getRequestURI();
        if (url.contains("login")||url.contains("register")||url.contains("checkUsername")||url.contains("forgetPassWord")||url.contains("checkEmail")){
            log.info("登录相关操作，放行：{}",url);
            return true;
        }
        String jwt = req.getHeader("token");
        if (!StringUtils.hasLength(jwt)) {
            log.info("JWT令牌为空，返回未登录的信息");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录的令牌信息");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }
        log.info("令牌合法，放行");
        return true;
    }
}
