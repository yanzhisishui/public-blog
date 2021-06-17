package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.info.Banner;
import com.syc.blog.entity.info.Skill;
import com.syc.blog.mapper.info.SkillMapper;
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
@RequestMapping("/skill")
public class SkillController {

    @Autowired
    SkillMapper skillMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "skill/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<Skill> iPage = new Page<>(page,pageSize);
        IPage<Skill> bannerIPage = skillMapper.selectPage(iPage, Wrappers.<Skill>lambdaQuery().eq(Skill::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(Skill skill){
        skill.setDateInsert(new Date());
        int row = skillMapper.insert(skill);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Skill skill){
        skill.setDateUpdate(new Date());
        int row = skillMapper.updateById(skill);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "skill/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        Skill skill=skillMapper.selectById(id);
        map.put("skill",skill);
        return "skill/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Skill skill = new Skill();
        skill.setDateUpdate(new Date());
        skill.setId(id);
        skill.setArchive((byte)1);
        int row=skillMapper.updateById(skill);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
