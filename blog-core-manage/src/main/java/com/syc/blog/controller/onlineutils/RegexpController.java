package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.onlineutils.Regexp;
import com.syc.blog.mapper.onlineutils.RegexpMapper;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/util/regexp")
public class RegexpController {

    @Autowired
    RegexpMapper regexpMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "onlineutils/regexp/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<Regexp> iPage = new Page<>(page,pageSize);
        IPage<Regexp> bannerIPage = regexpMapper.selectPage(iPage, Wrappers.<Regexp>lambdaQuery().eq(Regexp::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(Regexp regexp){
        regexp.setDateInsert(new Date());
        int row = regexpMapper.insert(regexp);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Regexp regexp){
        regexp.setDateUpdate(new Date());
        int row = regexpMapper.updateById(regexp);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "onlineutils/regexp/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        Regexp regexp=regexpMapper.selectById(id);
        map.put("regexp",regexp);
        return "onlineutils/regexp/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Regexp regexp = new Regexp();
        regexp.setDateUpdate(new Date());
        regexp.setId(id);
        regexp.setArchive((byte)1);
        int row=regexpMapper.updateById(regexp);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
