package com.syc.blog.rabbitmq;

import com.syc.blog.constants.Constant;
import com.syc.blog.entity.article.Article;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 增加文章浏览量消息
     * */
    public void articleBrowserCountMessage(Article article){
        rabbitTemplate.convertAndSend(Constant.RABBITMQ_EXCHANGE,"",article);
    }

}
