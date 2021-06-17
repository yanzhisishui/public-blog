package com.syc.blog.controller.article;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.mapper.article.ArticleClassifyMapper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/article/classify")
public class ArticleClassifyController {

    @Autowired
    ArticleClassifyMapper articleClassifyMapper;

    @RequestMapping("/manage")
    public String manage(){
        return "articleclassify/manage";
    }


    @RequestMapping("/selectList")
    @ResponseBody
    public String selectList(){
        List<ArticleClassify> articleClassifies = articleClassifyMapper.selectList(Wrappers.<ArticleClassify>lambdaQuery().eq(ArticleClassify::getArchive, 0));
        ResultHelper result = ResultHelper.wrapSuccessfulResult(articleClassifies);
        return JSON.toJSONString(result);
    }


    @RequestMapping("/add")
    public String add(@RequestParam("id") Integer parentId, ModelMap map){
        ArticleClassify parent = null;
        if(parentId == 0){
            parent = new ArticleClassify();
            parent.setId(0);
            parent.setName("");
            parent.setLevel((byte)0);
        }else{
            parent = articleClassifyMapper.selectById(parentId);
        }
        map.put("parent",parent);
        return "articleclassify/add";
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(ArticleClassify ArticleClassify){
        ArticleClassify.setDateInsert(new Date());
        int row = articleClassifyMapper.insert(ArticleClassify);
        ResultHelper result = ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(ArticleClassify articleClassify){
        articleClassify.setDateUpdate(new Date());
        int row = articleClassifyMapper.updateById(articleClassify);
        ResultHelper result = ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }


    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id,@RequestParam("parentId") Integer parentId,ModelMap map){
        ArticleClassify articleClassify = articleClassifyMapper.selectById(id);
        ArticleClassify parent = null;
        if(parentId == 0){
            parent = new ArticleClassify();
            parent.setId(0);
            parent.setName("");
            parent.setLevel((byte)0);
        }else{
            parent = articleClassifyMapper.selectById(parentId);
        }
        map.put("articleClassify",articleClassify);
        map.put("parent",parent);
        return "articleclassify/edit";
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        ArticleClassify articleClassify = new ArticleClassify();
        articleClassify.setId(id);
        articleClassify.setArchive((byte)1);
        articleClassify.setDateUpdate(new Date());
        int row = articleClassifyMapper.updateById(articleClassify);
        ResultHelper result = ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
