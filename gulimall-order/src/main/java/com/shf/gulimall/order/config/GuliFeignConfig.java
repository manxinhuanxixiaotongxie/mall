package com.shf.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: feign拦截器功能
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-02 21:10
 **/

@Configuration
public class GuliFeignConfig {

    /**
     * 解决feign远程调用丢失请求头问题
     *
     * @return
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {

        //1、使用RequestContextHolder拿到刚进来的请求数据
        //老请求
        //2、同步请求头的数据（主要是cookie）
        //把老请求的cookie值放到新请求上来，进行一个同步

        return template -> {
            //1、使用RequestContextHolder拿到刚进来的请求数据
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                //老请求
                HttpServletRequest request = requestAttributes.getRequest();

                //2、同步请求头的数据（主要是cookie）
                //把老请求的cookie值放到新请求上来，进行一个同步
                String cookie = request.getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }

}
