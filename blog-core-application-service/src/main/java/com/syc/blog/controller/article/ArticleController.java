package com.syc.blog.controller.article;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.constants.Constant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.entity.article.UserPraiseArticle;
import com.syc.blog.entity.info.OnlineUtils;
import com.syc.blog.entity.user.User;
import com.syc.blog.mapper.article.UserPraiseArticleMapper;
import com.syc.blog.rabbitmq.MQProducer;
import com.syc.blog.repository.ArticleRepository;
import com.syc.blog.service.article.ArticleClassifyService;
import com.syc.blog.service.info.OnlineUtilsService;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/article")
@Slf4j
public class ArticleController extends BaseController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    OnlineUtilsService onlineUtilsService;
    @Autowired
    ArticleClassifyService articleClassifyService;
    @Autowired
    UserPraiseArticleMapper userPraiseArticleMapper;
    @Autowired
    MQProducer mqProducer;
    @RequestMapping("/{id}")
    public String chapter(@PathVariable("id") Integer id, ModelMap map,
                          @RequestParam(value = "page",required = false,defaultValue = "1") Integer page, HttpServletRequest request){

        String ipAddress = StringHelper.getIpAddress(request);
        log.info("ip:{} 访问文章:{}",ipAddress,id);

        Article article = articleRepository.findById(id).orElse(null);

        map.put("pre",recursionFindArticle(id,0));
        map.put("next",recursionFindArticle(id,1));

        List<OnlineUtils> onlineUtilsList = onlineUtilsService.selectListLatest(5);
        map.put("onlineUtilsList",onlineUtilsList);
        List<ArticleClassify> articleClassifyList = articleClassifyService.selectListByLevel(1);
        map.put("articleClassifyList",articleClassifyList);

        String ua= request.getHeader("User-Agent");
        if(StringHelper.checkAgentIsMobile(ua)){ //验证手机端登录
            map.put("isMobile",true);
        }
        //浏览量增加
        mqProducer.articleBrowserCountMessage(article);

        byte type = Constant.USER_COMMENT_TYPE_ARTICLE;
        getCurrentCommentsListPage(map,page,id,type);

        User loginUser = getLoginUser(request);
        //查询用户是否点过赞
        boolean flag  =userIsPraised(loginUser,id);
        //从redis查询文章点赞数量
        Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE_COUNT, id.toString());
        if(o != null){ //如果redis中有。说明还没有同步到mysql和es，此时取(redis中的点赞数+es中已同步的点赞数)
            article.setPraise((Integer)o + article.getPraise());
        }
        map.put("article",article);
        map.put("articlePraised",flag);


        return "article";
    }

    //判断用户是否点过赞
    private boolean userIsPraised(User loginUser, Integer id) {
        if(loginUser == null){
           return false;
        }else{
            Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE, loginUser.getId()+"::"+id);
            if(o == null){ //redis没有，去数据库查
                UserPraiseArticle upa = userPraiseArticleMapper.selectOne(Wrappers.<UserPraiseArticle>lambdaQuery().eq(UserPraiseArticle::getArticleId, id).eq(UserPraiseArticle::getUserId, loginUser.getId()));
                if(upa != null){
                    return upa.getStatus() == 1;
                }else{ //数据库也没有,说明没点过赞
                    return false;
                }
            }
            else{
                Integer v = (Integer) o;
                return v == 1 ;
            }
        }
    }

    /**
     * flag = 0 :反向查找
     * flag = 1 :正向查找
     * 递归查找上一篇下一篇文章
     * */
    Article recursionFindArticle(int id,int flag){
        if(id < 1 || id > 100){
            Article a = new Article();
            a.setTitle("");
            return a;
        }
        id = flag == 0 ? id - 1 : id +1;
        Article article = articleRepository.findById(id).orElse(null);
        if(article == null){
            return recursionFindArticle(id,flag);
        }
        return article;
    }


    /**
     * 用户点赞
     * */
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/praise")
    @ResponseBody
    public String praise(@RequestParam("articleId") Integer articleId,HttpServletRequest request){
        User loginUser = getLoginUser(request);
        ResultHelper result = null;
        if(loginUser == null){
            result = ResultHelper.wrapErrorResult(1,"请先登录");
            return JSON.toJSONString(result);
        }

        String userId = loginUser.getId().toString();
        String key = userId + "::" + articleId;
        log.info("文章点赞存入Redis开始，articleId:{},userId:{}",articleId,loginUser.getId());
        synchronized (this){
            Boolean flag = redisTemplate.hasKey(RedisConstant.ARTICLE_PRAISE);
            if(flag != null && flag){
                Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.ARTICLE_PRAISE);
                if(map.containsKey(key)){ //已存在
                    Integer v = (Integer)map.get(key);
                    redisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE, key,v == 1 ? 0 : 1);
                    redisTemplate.opsForHash().increment(RedisConstant.ARTICLE_PRAISE_COUNT, articleId.toString(),v == 1 ? -1 : 1);

                }else{ //点赞
                    redisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE, key,1);
                    redisTemplate.opsForHash().increment(RedisConstant.ARTICLE_PRAISE_COUNT, articleId.toString(),1);

                }
            }else{
                redisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE, key,1);
                redisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE_COUNT, articleId.toString(),1);
            }
           /* Boolean flag = stringRedisTemplate.hasKey(RedisConstant.ARTICLE_PRAISE);
            Article article = articleRepository.findById(articleId).orElse(null);
            if(flag != null && flag) {
                Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(RedisConstant.ARTICLE_PRAISE);//拿到map
                Object o = map.get(articleId.toString());
                if(o == null){ //
                    Set<String> set =new HashSet<>();
                    set.add(loginUser.getId().toString());
                    stringRedisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE,articleId.toString(),JSON.toJSONString(set));
                    article.setPraise(article.getPraise() + 1);
                    articleRepository.save(article);
                    result = ResultHelper.wrapSuccessfulResult(null);
                    return JSON.toJSONString(result);
                }
                HashSet<String> set = JSON.parseObject(o.toString(),HashSet.class);
                if(set.contains(loginUser.getId().toString())){
                    set.remove(loginUser.getId().toString());//点过赞取消
                    stringRedisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE,articleId.toString(),JSON.toJSONString(set));
                    article.setPraise(article.getPraise() - 1);
                    articleRepository.save(article);
                    result = ResultHelper.wrapErrorResult(2,"已经点过赞啦");
                    return JSON.toJSONString(result);
                }else{
                    set.add(loginUser.getId().toString());
                    stringRedisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE,articleId.toString(),JSON.toJSONString(set));
                    article.setPraise(article.getPraise() + 1);
                    articleRepository.save(article);
                }
            }else{
                Set<String> set =new HashSet<>();
                set.add(loginUser.getId().toString());
                stringRedisTemplate.opsForHash().put(RedisConstant.ARTICLE_PRAISE,articleId.toString(),JSON.toJSONString(set));
                article.setPraise(article.getPraise() + 1);
                articleRepository.save(article);
            }*/
        }
        result = ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
