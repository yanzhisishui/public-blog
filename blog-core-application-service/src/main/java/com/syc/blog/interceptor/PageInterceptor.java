package com.syc.blog.interceptor;

import com.alibaba.fastjson.JSON;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.user.CardInfo;
import com.syc.blog.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 每次渲染页面时放入公共信息
 * */
@Slf4j
public class PageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = StringHelper.getIpAddress(request);
        log.info("ip:"+ip+",访问："+((HandlerMethod) handler).getMethod().getName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        //网站logo
        String logoName = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_LOGO_URL);
        request.setAttribute("logoUrl",logoName);
        //图标地址
        String iconfontUrl = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_ICONFONT_URL);
        request.setAttribute("iconfontUrl",iconfontUrl);
        //个人信息
        String cardStr = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_CARD_INFO);
        CardInfo card = JSON.parseObject(cardStr, CardInfo.class);
        request.setAttribute("card",card);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
