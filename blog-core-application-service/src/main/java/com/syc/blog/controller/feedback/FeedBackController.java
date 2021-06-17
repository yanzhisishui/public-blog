package com.syc.blog.controller.feedback;

import com.alibaba.fastjson.JSON;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.user.User;
import com.syc.blog.feedback.FeedBack;
import com.syc.blog.mapper.feedback.FeedBackMapper;
import com.syc.blog.service.feedback.FeedBackService;
import com.syc.blog.utils.DateHelper;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/feedback")
@Slf4j
public class FeedBackController extends BaseController {
    @Autowired
    FeedBackService feedBackService;
    @Autowired
    FeedBackMapper feedBackMapper;

    @RequestMapping
    public String feedback(){
        return "feedback";
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(FeedBack feedBack, HttpServletRequest request){
        User loginUser = getLoginUser(request);
        ResultHelper result;
        if(loginUser == null){
            result= ResultHelper.wrapErrorResult(1,"请先登录");
            return JSON.toJSONString(result);
        }
        boolean illegal = StringHelper.hasIllegal(feedBack.getContent());
        if(illegal){
            result= ResultHelper.wrapErrorResult(3,"请文明发言");
            return JSON.toJSONString(result);
        }
        //判断当前用户当日反馈的次数是不是超过上限
        feedBack.setUserId(loginUser.getId());
        Integer count = feedBackMapper.selectTodayCountByUserId(feedBack.getUserId());
        if(count >= 5){
            log.info("用户：{} 今日：{} 反馈次数已经达到五次",feedBack.getUserId(), DateHelper.getDateStr(new Date(),"yyyy-MM-dd"));
            result= ResultHelper.wrapErrorResult(4,"当日反馈次数已达到上限");
            return JSON.toJSONString(result);
        }
        feedBack.setDateInsert(new Date());
        int row=feedBackService.save(feedBack);
        result=row == 0 ? ResultHelper.wrapErrorResult(2,"反馈失败") : ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }
}
