package com.syc.blog.controller;

import com.alibaba.fastjson.JSON;
import com.syc.blog.constants.Constant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.info.OnlineUtils;
import com.syc.blog.entity.user.CardInfo;
import com.syc.blog.service.OnlineUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    OnlineUtilsService onlineUtilsService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/")
    public String index(ModelMap map){
        List<OnlineUtils> list =  onlineUtilsService.selectList();
        map.put("onlineutilsList",list);

        putPageCommon(map);
        return "index";
    }

    @RequestMapping("/login/exit")
    public String exit(HttpSession session){
        session.setAttribute(Constant.USER_LOGIN_SESSION_KEY,null);
        return "redirect:/";
    }


    /**
     * 注入页面的redis常用值
     * */
    public void putPageCommon(ModelMap map){
        //网站logo
        String logoName = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_LOGO_URL);
        map.put("logoUrl",logoName);
        //图标地址
        String iconfontUrl = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_ICONFONT_URL);
        map.put("iconfontUrl",iconfontUrl);

        String cardStr = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_CARD_INFO);
        CardInfo card = JSON.parseObject(cardStr, CardInfo.class);
        map.put("card",card);

    }
}
