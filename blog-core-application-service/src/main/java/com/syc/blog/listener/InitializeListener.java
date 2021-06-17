package com.syc.blog.listener;

import com.syc.blog.constants.Constant;
import com.syc.blog.entity.config.BaseConfig;
import com.syc.blog.entity.user.CardInfo;
import com.syc.blog.service.article.ArticleService;
import com.syc.blog.service.config.BaseConfigService;
import com.syc.blog.utils.IllegalWordsHelper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitializeListener implements ServletContextListener {

    @Autowired
    ArticleService articleService;
    @Autowired
    BaseConfigService baseConfigService;
    @Autowired
    AmqpAdmin amqpAdmin;

    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        //初始化文章浏览量消息队列
        amqpAdmin.declareExchange(new DirectExchange(Constant.RABBITMQ_EXCHANGE));//创建交换器
        amqpAdmin.declareQueue(new Queue(Constant.RABBITMQ_QUEUE,true));//创建队列
        amqpAdmin.declareBinding(new Binding(Constant.RABBITMQ_QUEUE,Binding.DestinationType.QUEUE,Constant.RABBITMQ_EXCHANGE,"",null));

        //初始化数据到ES
        articleService.initRepository();
        //初始化数据到Redis
        baseConfigService.initRedis();
        //初始化敏感词库
        IllegalWordsHelper.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
