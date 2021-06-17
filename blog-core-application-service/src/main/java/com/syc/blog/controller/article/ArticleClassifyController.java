package com.syc.blog.controller.article;

import com.alibaba.fastjson.JSON;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.service.article.ArticleClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/article/classify")
public class ArticleClassifyController {
    @Autowired
    ArticleClassifyService articleClassifyService;
    @RequestMapping("/selectByParentId")
    @ResponseBody
    public String selectByParentId(@RequestParam("id") Integer id){
        List<ArticleClassify> list =  articleClassifyService.selectListByParentId(id);
        return JSON.toJSONString(list);
    }

    @RequestMapping("/selectIdTree")
    @ResponseBody
    public String selectIdTree(@RequestParam("id") Integer id){
        String str = articleClassifyService.selectIdTree(id);
        return str;
    }
}
