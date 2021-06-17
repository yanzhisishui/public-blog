package com.syc.blog;

import com.syc.blog.config.ApplicationConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

@SpringBootApplication
@EnableRabbit //开启rabbitMQ注解
@EnableScheduling //开启定时任务
@ServletComponentScan
@MapperScan(basePackages = {"com.syc.blog.mapper"})//扫描@Mapper注解
@EnableRedisHttpSession //交给redis
//@EnableCaching
public class BlogCoreAppServiceApplication {
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors","false");//解决redis和elasticsearch冲突
        SpringApplication.run(BlogCoreAppServiceApplication.class,args);
    }

    @Autowired
    ApplicationConfig applicationConfig;
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();

        //不加前缀，实现子域名session共享
        cookieSerializer.setDomainName(applicationConfig.getDomainName());
        cookieSerializer.setCookiePath("/");
        cookieSerializer.setUseBase64Encoding(false);
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);
        return cookieHttpSessionIdResolver;
    }

}
