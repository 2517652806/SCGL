package com.cddr.szd.config;

import com.cddr.szd.login.LoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author ZY
 */
@Configuration
public class LoginFilterConfig extends WebMvcConfigurationSupport {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器，设置路径
        registry.addInterceptor(
                        new LoginFilter())
                //需要拦截的路径
                .addPathPatterns("/api/**")
                //不需要拦截的路径 （登录接口不需要拦截）
                .excludePathPatterns("/api/login/**");
        super.addInterceptors(registry);
    }
}
