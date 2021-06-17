package com.syc.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = {"com.syc.blog.mapper"})//扫描@Mapper注解
@EnableRedisHttpSession //交给redis
//@EnableCaching
public class BlogCoreOnlineUtilsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogCoreOnlineUtilsServiceApplication.class,args);
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("sunyuchao.com");
        cookieSerializer.setCookiePath("/");
        cookieSerializer.setUseBase64Encoding(false);
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);
        return cookieHttpSessionIdResolver;
    }

}
