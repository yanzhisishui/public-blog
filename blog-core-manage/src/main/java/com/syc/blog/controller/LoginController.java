package com.syc.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.utils.MD5Helper;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.constant.Constant;
import com.syc.blog.entity.admin.AdminUser;
import com.syc.blog.mapper.admin.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    AdminUserMapper adminUserMapper;
    @RequestMapping("/")
    public String login(HttpSession session){
        Object loginAdmin = session.getAttribute(Constant.ADMIN_USER_LOGIN_SESSION_KEY);
        if(loginAdmin == null){
            return "login";
        }
        return "index";
    }

    @RequestMapping("/loginCheck")
    @ResponseBody
    public String loginCheck(@RequestParam("username") String username,
                             @RequestParam("password") String password,HttpSession session){
        AdminUser au = adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery()
                .eq(AdminUser::getUsername, username));
        ResultHelper result = null;
        if(au == null){
            result = ResultHelper.wrapErrorResult(1,"用户不存在");
        } else if(!au.getPassword().equals(MD5Helper.encrypt(password))){
            result = ResultHelper.wrapErrorResult(1,"密码错误");
        } else {
            result = ResultHelper.wrapSuccessfulResult(null);
            session.setAttribute(Constant.ADMIN_USER_LOGIN_SESSION_KEY,au);
        }
        return JSON.toJSONString(result);
    }
}
