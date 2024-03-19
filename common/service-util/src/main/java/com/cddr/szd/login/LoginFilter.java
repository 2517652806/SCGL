package com.cddr.szd.login;


import com.cddr.szd.helper.JWTHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ZY
 */
//登录拦截器
public class LoginFilter implements HandlerInterceptor {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null){
            response.setStatus(401);
            return false;
        }

        //验证token是否合法
        try {
            Integer userId = JWTHelper.getUserId(token);
            String s = stringRedisTemplate.opsForValue().get(userId);
            if (s == null || !s.equals(token)){
                throw new RuntimeException("token失效");
            }

            ThreadLocalUtil.set(token);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(401);
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求结束清除本地线程的数据防止内存泄露
        ThreadLocalUtil.remove();
    }
}
