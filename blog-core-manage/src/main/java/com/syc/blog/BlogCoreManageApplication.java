package com.syc.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.syc.blog.mapper"})//扫描@Mapper注解
public class BlogCoreManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogCoreManageApplication.class, args);
    }

}
