package com.syc.blog.controller.comment;

import com.alibaba.fastjson.JSON;
import com.syc.blog.constants.Constant;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.entity.user.User;
import com.syc.blog.service.comment.UserCommentService;
import com.syc.blog.service.user.UserService;
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
@RequestMapping("/userComment")
@Slf4j
public class UserCommentController extends BaseController {

    @Autowired
    UserCommentService userCommentService;
    @Autowired
    UserService userService;

    @RequestMapping("/saveChildrenMessage")
    @ResponseBody
    public String saveChildrenMessage(UserComment comment, HttpServletRequest request){
        ResultHelper<UserComment.ChildrenMessageTemplate> result;
        /**
         * 验证评论条件
         * */
        String val = commentCheck(comment,request);
        if(val != null){
            result= ResultHelper.wrapErrorResult(1,val);
            return JSON.toJSONString(result);
        }
        comment.setDateInsert(new Date());
        //下面进行防Xss
        String content = StringHelper.filterXSS(comment.getContent());
        comment.setContent(content);
        int row=userCommentService.save(comment);
        if(row != 0){
            UserComment.ChildrenMessageTemplate childrenMessageTemplate =new UserComment.ChildrenMessageTemplate();
            User commentUser = userService.selectById(comment.getUserId());
            childrenMessageTemplate.setAvatar(commentUser.getAvatar());
            childrenMessageTemplate.setUserId(commentUser.getId());
            childrenMessageTemplate.setCommentUsername(commentUser.getNickname());
            User commentedUser = userService.selectById(comment.getCommentedUserId());
            childrenMessageTemplate.setCommentedUsername(commentedUser.getNickname());
            childrenMessageTemplate.setContent(comment.getContent());
            childrenMessageTemplate.setTime(DateHelper.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
            result= ResultHelper.wrapSuccessfulResult(childrenMessageTemplate);
            return JSON.toJSONString(result);
        }
        result= ResultHelper.wrapErrorResult(1,"回复失败");
        return JSON.toJSONString(result);
    }



    @RequestMapping("/saveFirstMessage")
    @ResponseBody
    public String saveFirstMessage(UserComment comment, HttpServletRequest request){
        ResultHelper<UserComment.FirstMessageTemplate> result;
        String val = commentCheck(comment, request);
        if(val != null){
            result= ResultHelper.wrapErrorResult(2,val);
            return JSON.toJSONString(result);
        }
        comment.setDateInsert(new Date());
        //下面进行防Xss
        String content = StringHelper.filterXSS(comment.getContent());
        comment.setContent(content);
        int row=userCommentService.save(comment);
        if(row != 0){

            UserComment.FirstMessageTemplate firstMessageTemplate =new UserComment.FirstMessageTemplate();
            User commentUser = userService.selectById(comment.getUserId());
            firstMessageTemplate.setAvatar(commentUser.getAvatar());
            firstMessageTemplate.setTime(DateHelper.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
            firstMessageTemplate.setCommentId(comment.getUserId());
            firstMessageTemplate.setCommentUsername(commentUser.getNickname());
            firstMessageTemplate.setCommentUsernameCopy(firstMessageTemplate.getCommentUsername());
            firstMessageTemplate.setContent(comment.getContent());
            result= ResultHelper.wrapSuccessfulResult(firstMessageTemplate);
            return JSON.toJSONString(result);
        }
        result= ResultHelper.wrapErrorResult(2,"回复失败");
        return JSON.toJSONString(result);
    }
}
