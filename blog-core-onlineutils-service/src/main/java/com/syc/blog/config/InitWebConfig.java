package com.syc.blog.config;


import com.syc.blog.interceptor.PageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InitWebConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 页面请求拦截器
         * */
        registry.addInterceptor(new PageInterceptor())
                .addPathPatterns("/","/util/idCardCode","/util/imgPress","/util/jsonFormat",
                        "/util/linux","/util/numMoneyConvert","/util/qrCode","/util/quartz",
                        "/util/regexp","/util/timestamp","/util/sqlToJava","/util/watermark","/util/mortgage/loan","/util/bmi");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    /**
     * 添加静态资源过滤
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }


}
