package com.syc.blog.controller.article;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.constant.Constant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.mapper.article.ArticleClassifyMapper;
import com.syc.blog.mapper.article.ArticleMapper;
import com.syc.blog.mapper.comment.UserCommentMapper;
import com.syc.blog.repository.ArticleRepository;
import com.syc.blog.service.ArticleService;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleClassifyMapper articleClassifyMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ArticleService articleService;
    @RequestMapping("/manage")
    public String manage(){
        return "article/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize,@RequestParam (value = "title",required = false) String title){
        IPage<Article> iPage = new Page<>(page,pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("title",title);
        IPage<Article> bannerIPage = articleMapper.queryListPage(iPage,params);
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save( Article article){
        article.setDateInsert(new Date());
        String bread = articleClassifyMapper.selectNameTree(article.getClassifyId()).replace(",", "/");
        article.setBread(bread);
        int row = articleMapper.insert(article);
        if(row != 0){
            articleService.syncToElasticsearch(articleMapper.selectById(article.getId()));
        }
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Article article){
        article.setDateUpdate(new Date());
        String bread = articleClassifyMapper.selectNameTree(article.getClassifyId()).replace(",", "/");
        article.setBread(bread);
        int row = articleMapper.updateById(article);
        if(row != 0){
            String s = stringRedisTemplate.opsForValue().get(RedisConstant.ARTICLE_RECOMMEND);
            List<Article> articleList = JSON.parseArray(s, Article.class);
            if(articleList != null){
                for(Article aa : articleList){
                    if(aa.getId().equals(article.getId())){
                        aa.setTitle(article.getTitle());
                    }
                }
            }
            stringRedisTemplate.opsForValue().set(RedisConstant.ARTICLE_RECOMMEND,JSON.toJSONString(articleList));
            //更新es
            articleService.syncToElasticsearch(articleMapper.selectById(article.getId()));
        }
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "article/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        Article article=articleMapper.selectById(id);
        map.put("article",article);
        String s = articleClassifyMapper.selectIdTree(article.getClassifyId());
        List<String> list = new ArrayList<>(Arrays.asList(s.split(",")));
        map.put("list",list);
        return "article/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Article article = new Article();
        article.setDateUpdate(new Date());
        article.setId(id);
        article.setArchive((byte)1);
        int row=articleMapper.updateById(article);
        articleRepository.deleteById(article.getId());
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    /**
     * 添加到推荐
     * */
    @RequestMapping("/recommend")
    @ResponseBody
    public String recommend(@RequestParam("id") Integer id){
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        QueryWrapper<Article> qw = new QueryWrapper<>();
        qw.select("id","title").eq("id",id);
        Article article = articleMapper.selectOne(qw);
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.ARTICLE_RECOMMEND);
        List<Article> articleList =new ArrayList<>();;
        if(s != null){
            articleList = JSON.parseArray(s, Article.class);
            if(articleList.contains(article)){
                result = ResultHelper.wrapErrorResult(1,"不可重复推荐");
                return JSON.toJSONString(result);
            }
            articleList.add(article);
        }else{
            articleList.add(article);
        }
        stringRedisTemplate.opsForValue().set(RedisConstant.ARTICLE_RECOMMEND,JSON.toJSONString(articleList));
        return JSON.toJSONString(result);
    }

    @RequestMapping("/recommend/manage")
    public String recommendPage(){
        return "article/recommend_list";
    }

    @RequestMapping("/queryRecommendList")
    @ResponseBody
    public String queryRecommendList(){
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.ARTICLE_RECOMMEND);
        List<Article> articleList = JSON.parseArray(s, Article.class);
        return JsonHelper.objectToJsonForTable(articleList,10L);
    }

    @RequestMapping("/deleteRecommend")
    @ResponseBody
    public String deleteRecommend(@RequestParam("id") Integer id){
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.ARTICLE_RECOMMEND);
        List<Article> articleList = JSON.parseArray(s, Article.class);
        articleList.removeIf(next -> next.getId().equals(id));
        stringRedisTemplate.opsForValue().set(RedisConstant.ARTICLE_RECOMMEND,JSON.toJSONString(articleList));
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(null));
    }

    @Autowired
    UserCommentMapper userCommentMapper;
    @Autowired
    ArticleRepository articleRepository;

    @RequestMapping("/initializeESArticle")
    @ResponseBody
    public String initializeESArticle(){
        List<Article> list =  articleMapper.selectList(Wrappers.<Article>lambdaQuery().eq(Article::getArchive,0));
        for(Article article : list){
            ArticleClassify articleClassify = articleClassifyMapper.selectById(article.getClassifyId());
            log.info("当前文章：id:{},title:{}",article.getId(),article.getTitle());
            article.setClassify(articleClassify);
            Integer count = userCommentMapper.selectCount(Wrappers.<UserComment>lambdaQuery().eq(UserComment::getBindId, article.getId()).eq(UserComment::getType, 1));
            article.setCommentCount(count);
            article.setCollectionCount(0);
        }
        articleRepository.deleteAll();
        articleRepository.saveAll(list);
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(null));
    }
}
