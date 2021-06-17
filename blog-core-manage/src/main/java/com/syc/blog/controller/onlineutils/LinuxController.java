package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.onlineutils.Linux;
import com.syc.blog.mapper.onlineutils.LinuxMapper;
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
@RequestMapping("/util/linux")
public class LinuxController {

    @Autowired
    LinuxMapper linuxMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "onlineutils/linux/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<Linux> iPage = new Page<>(page,pageSize);
        IPage<Linux> bannerIPage = linuxMapper.selectPage(iPage, Wrappers.<Linux>lambdaQuery().eq(Linux::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(Linux linux){
        linux.setDateInsert(new Date());
        int row = linuxMapper.insert(linux);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Linux linux){
        linux.setDateUpdate(new Date());
        int row = linuxMapper.updateById(linux);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "onlineutils/linux/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        Linux linux=linuxMapper.selectById(id);
        map.put("linux",linux);
        return "onlineutils/linux/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Linux linux = new Linux();
        linux.setDateUpdate(new Date());
        linux.setId(id);
        linux.setArchive((byte)1);
        int row=linuxMapper.updateById(linux);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
