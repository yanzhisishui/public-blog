package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.info.MicroDiary;
import com.syc.blog.mapper.info.MicroDiaryMapper;
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
@RequestMapping("/microdiary")
public class MicroDiaryController {
    @Autowired
    MicroDiaryMapper microDiaryMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "microdiary/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<MicroDiary> iPage = new Page<>(page,pageSize);
        IPage<MicroDiary> bannerIPage = microDiaryMapper.selectPage(iPage, Wrappers.<MicroDiary>lambdaQuery().eq(MicroDiary::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(MicroDiary microDiary){
        microDiary.setDateInsert(new Date());
        int row = microDiaryMapper.insert(microDiary);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(MicroDiary microDiary){
        microDiary.setDateUpdate(new Date());
        int row = microDiaryMapper.updateById(microDiary);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "microdiary/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        MicroDiary microDiary=microDiaryMapper.selectById(id);
        map.put("microDiary",microDiary);
        return "microdiary/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        MicroDiary microDiary = new MicroDiary();
        microDiary.setDateUpdate(new Date());
        microDiary.setId(id);
        microDiary.setArchive((byte)1);
        int row=microDiaryMapper.updateById(microDiary);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
