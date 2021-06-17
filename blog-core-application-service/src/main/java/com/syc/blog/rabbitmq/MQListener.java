package com.syc.blog.rabbitmq;

import com.syc.blog.constants.Constant;
import com.syc.blog.entity.article.Article;
import com.syc.blog.mapper.article.ArticleMapper;
import com.syc.blog.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQListener {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ArticleRepository articleRepository;

    /**
     * 监听消息
     * */
    @RabbitListener(queues = Constant.RABBITMQ_QUEUE)
    public void receiveIncreaseBrowserCountMessage(Article article) {
        //收到消息执行浏览量增加
        log.info("消息队列增加文章浏览量开始");
        int row = articleMapper.increaseBrowser(article);
        if(row != 0){
            //存进es
            article.setBrowser(article.getBrowser() + 1);
            articleRepository.save(article);
            log.info("消息队列增加文章浏览量并且同步到ES结束");
        }
    }
}
