package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.info.OnlineUtils;
import com.syc.blog.mapper.info.OnlineUtilsMapper;
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
@RequestMapping("/onlineutils")
public class OnlineUtilsController {

    @Autowired
    OnlineUtilsMapper onlineutilsMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "onlineutils/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<OnlineUtils> iPage = new Page<>(page,pageSize);
        IPage<OnlineUtils> bannerIPage = onlineutilsMapper.selectPage(iPage, Wrappers.<OnlineUtils>lambdaQuery().eq(OnlineUtils::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(OnlineUtils onlineutils){
        onlineutils.setDateInsert(new Date());
        int row = onlineutilsMapper.insert(onlineutils);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(OnlineUtils onlineutils){
        onlineutils.setDateUpdate(new Date());
        int row = onlineutilsMapper.updateById(onlineutils);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "onlineutils/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        OnlineUtils onlineutils=onlineutilsMapper.selectById(id);
        map.put("onlineutils",onlineutils);
        return "onlineutils/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        OnlineUtils onlineutils = new OnlineUtils();
        onlineutils.setDateUpdate(new Date());
        onlineutils.setId(id);
        onlineutils.setArchive((byte)1);
        int row=onlineutilsMapper.updateById(onlineutils);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
