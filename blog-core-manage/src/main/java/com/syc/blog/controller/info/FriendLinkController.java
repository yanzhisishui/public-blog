package com.syc.blog.controller.info;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.entity.info.FriendLink;
import com.syc.blog.mapper.info.FriendLinkMapper;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequestMapping("/friend")
@Controller
public class FriendLinkController {

    @Autowired
    FriendLinkMapper friendLinkMapper;

    @RequestMapping("/manage")
    public String manage(){
        return "friendlink/manage";
    }
    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<FriendLink> iPage = new Page<>(page,pageSize);
        IPage<FriendLink> bannerIPage = friendLinkMapper.selectPage(iPage, Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(FriendLink friendLink){
        friendLink.setDateInsert(new Date());
        int row = friendLinkMapper.insert(friendLink);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(FriendLink friendLink){
        friendLink.setDateUpdate(new Date());
        int row = friendLinkMapper.updateById(friendLink);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "friendlink/add";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        FriendLink friendLink=friendLinkMapper.selectById(id);
        map.put("friendLink",friendLink);
        return "friendlink/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        FriendLink friendLink = new FriendLink();
        friendLink.setDateUpdate(new Date());
        friendLink.setId(id);
        friendLink.setArchive((byte)1);
        int row=friendLinkMapper.updateById(friendLink);
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
