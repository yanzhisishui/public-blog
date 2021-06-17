package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.info.Notice;
import com.syc.blog.mapper.info.NoticeMapper;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequestMapping("/notice")
@Controller
public class NoticeController {
    @Autowired
    NoticeMapper noticeMapper;
    @RequestMapping("/manage")
    public String manage(){
        return "notice/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<Notice> iPage = new Page<>(page,pageSize);
        IPage<Notice> bannerIPage = noticeMapper.selectPage(iPage, Wrappers.<Notice>lambdaQuery().eq(Notice::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(Notice notice){
        notice.setDateInsert(new Date());
        int row = noticeMapper.insert(notice);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Notice notice){
        notice.setDateUpdate(new Date());
        int row = noticeMapper.updateById(notice);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "notice/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        Notice notice=noticeMapper.selectById(id);
        map.put("notice",notice);
        return "notice/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        Notice notice = new Notice();
        notice.setDateUpdate(new Date());
        notice.setId(id);
        notice.setArchive((byte)1);
        int row=noticeMapper.updateById(notice);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
