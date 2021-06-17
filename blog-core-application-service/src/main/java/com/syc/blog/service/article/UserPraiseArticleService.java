package com.syc.blog.service.article;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.UserPraiseArticle;
import com.syc.blog.mapper.article.ArticleMapper;
import com.syc.blog.mapper.article.UserPraiseArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
public class UserPraiseArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserPraiseArticleMapper userPraiseArticleMapper;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    ArticleService articleService;
    //同步redis数据到mysql
    @Transactional
    public void syncRedisData() {
        List<UserPraiseArticle> upaList = getPraisedDataFromRedis();
       if(upaList.size() > 0){
           int row = userPraiseArticleMapper.saveList(upaList);
           List<Article> articles = getPraisedCountFromRedis();
           int row2 = articleMapper.updateList(articles);
           /**
            * 1.这样会存在问题，当mysql语句执行时是比较慢的相对。此时用户点赞，那么这个数据不会被同步到mysql
            * 2.如果在获取list的时候就删除redis，那么又会存在问题。当mysql语句执行报错时，spring事务会回滚。但是redis没有做事务
            * 3.考虑到每隔一个小时会同步数据，且是在准点时刻进行例如 12:00，可以在同步数据执行mysql的时候给redis加锁。
            *      然后点赞接口那里。判断时间是否已经快到整点，如果还差几秒到整点数据。那么尝试获取锁，等锁释放再执行点赞！完美
            * */
           //删除redis
           deletePraiseDataFromRedis();
           deletePraiseCountFromRedis();
           articleService.initRepository();//把点赞数更新到es
           log.info("同步点赞数据完成-----");
       }else{
           log.info("暂无数据需要同步-----");
       }
    }

    private void deletePraiseCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.ARTICLE_PRAISE_COUNT, ScanOptions.NONE);
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            String key = (String)map.getKey();
            redisTemplate.opsForHash().delete(RedisConstant.ARTICLE_PRAISE_COUNT, key);
        }
    }

    private void deletePraiseDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.ARTICLE_PRAISE, ScanOptions.NONE);
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            redisTemplate.opsForHash().delete(RedisConstant.ARTICLE_PRAISE, key);
        }
    }

    //查询用户点赞文章数据
    public List<UserPraiseArticle> getPraisedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.ARTICLE_PRAISE, ScanOptions.NONE);
        List<UserPraiseArticle> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 userId，articleId
            String[] split = key.split("::");
            String userId = split[0];
            String articleId = split[1];
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            UserPraiseArticle upa = new UserPraiseArticle();
            upa.setUserId(Integer.parseInt(userId));
            upa.setArticleId(Integer.parseInt(articleId));
            upa.setStatus(value.byteValue());
            upa.setDateInsert(new Date());
            list.add(upa);
        }

        return list;
    }


    public List<Article> getPraisedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.ARTICLE_PRAISE_COUNT, ScanOptions.NONE);
        List<Article> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            Integer value = (Integer)map.getValue();
            Article article = new Article();
            article.setId(Integer.parseInt(key));
            article.setPraise(value);
            list.add(article);
        }
        return list;
    }

}
