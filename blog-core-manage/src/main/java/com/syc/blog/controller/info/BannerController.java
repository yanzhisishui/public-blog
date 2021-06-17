package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.info.Banner;
import com.syc.blog.mapper.article.ArticleMapper;
import com.syc.blog.mapper.info.BannerMapper;
import com.syc.blog.utils.JsonHelper;
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
@RequestMapping("/banner")
public class BannerController  {

    @Autowired
    BannerMapper bannerMapper;
    @Autowired
    ArticleMapper articleMapper;
    @RequestMapping("/add")
    public String add(ModelMap map){
        List<Article> articleList=articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                .eq(Article::getArchive, 0));
        map.put("articleList",articleList);
        return "banner/add";
    }
    @RequestMapping("/manage")
    public String manage(){
        return "banner/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<Banner> iPage = new Page<>(page,pageSize);
        IPage<Banner> bannerIPage = bannerMapper.selectPage(iPage, Wrappers.<Banner>lambdaQuery().eq(Banner::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id")Integer id, ModelMap map){
        Banner banner=bannerMapper.selectById(id);
        map.put("banner",banner);
        List<Article> articleList=articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                .eq(Article::getArchive, 0));
        map.put("articleList",articleList);
        return "banner/edit";
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(Banner banner){
        banner.setDateInsert(new Date());
        int row=bannerMapper.insert(banner);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Banner banner = new Banner();
        banner.setDateUpdate(new Date());
        banner.setId(id);
        banner.setArchive((byte)1);
        int row=bannerMapper.updateById(banner);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Banner banner){
        banner.setDateUpdate(new Date());
        int row=bannerMapper.updateById(banner);
        ResultHelper result = ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

}
