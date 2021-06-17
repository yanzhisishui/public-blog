package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.syc.blog.constants.Constant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.entity.info.Notice;
import com.syc.blog.service.article.ArticleClassifyService;
import com.syc.blog.service.comment.UserCommentService;
import com.syc.blog.service.info.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/notice")
@Controller
@Slf4j
public class NoticeController extends BaseController {

    @Autowired
    ArticleClassifyService articleClassifyService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    UserCommentService userCommentService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/{id}")
    public String notice(@PathVariable("id") Integer id, ModelMap map,
                         @RequestParam(value = "page",required = false,defaultValue = "1") Integer page){

        Notice notice = noticeService.selectById(id);
        map.put("notice",notice);
        //查询上一篇下一篇
        List<Notice> noticeList = noticeService.selectNextAndPrev(id);
        String pre=null;
        String next=null;
        try{
            pre =noticeList.get(0).getTitle();
            next=noticeList.get(1).getTitle();
        }catch (Exception e){
            log.error("相邻通知被删除");
        }
        map.put("pre",pre);
        map.put("next",next);
        //查询最新评论
        List<UserComment> userCommentList = userCommentService.selectListLatest(5);
        map.put("latestCommentList",userCommentList);
        //查询推荐文章,从redis获取id
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.ARTICLE_RECOMMEND);
        List<Article> recommendArticleList = JSON.parseArray(s, Article.class);
        map.put("recommendArticleList",recommendArticleList);
        //查询热门标签
        List<ArticleClassify> hotTagList = articleClassifyService.selectHotTagList();
        map.put("hotTagList",hotTagList);
        byte type = Constant.USER_COMMENT_TYPE_NOTICE;
        getCurrentCommentsListPage(map,page,id,type);

        return "notice";
    }
}
