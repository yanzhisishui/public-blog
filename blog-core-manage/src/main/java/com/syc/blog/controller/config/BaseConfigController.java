package com.syc.blog.controller.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.constant.Constant;
import com.syc.blog.constants.GlobalConstant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.config.BaseConfig;
import com.syc.blog.entity.user.CardInfo;
import com.syc.blog.mapper.config.BaseConfigMapper;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/baseConfig")
public class BaseConfigController {

    @Autowired
    BaseConfigMapper baseConfigMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/manage")
    public String manage(){
        return "config/manage";
    }

    @RequestMapping("/queryListPage")
    @ResponseBody
    public String queryListPage(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<BaseConfig> iPage = new Page<>(page,pageSize);
        IPage<BaseConfig> bannerIPage = baseConfigMapper.selectPage(iPage, Wrappers.<BaseConfig>lambdaQuery().eq(BaseConfig::getArchive,0));
        return JsonHelper.objectToJsonForTable(bannerIPage.getRecords(),bannerIPage.getTotal());
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(BaseConfig baseConfig){
        baseConfig.setDateInsert(new Date());
        int row = baseConfigMapper.insert(baseConfig);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"添加技能失败") : ResultHelper.wrapSuccessfulResult(null);
        if(row != 0){
            //更新redis
            stringRedisTemplate.opsForValue().set(baseConfig.getName(),baseConfig.getValue());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(BaseConfig baseConfig){
        baseConfig.setDateUpdate(new Date());
        int row = baseConfigMapper.updateById(baseConfig);
        ResultHelper result= row == 0 ? ResultHelper.wrapErrorResult(1,"更新技能失败") : ResultHelper.wrapSuccessfulResult(null);
        if(row != 0){
            //更新redis
            stringRedisTemplate.opsForValue().set(baseConfig.getName(),baseConfig.getValue());
        }
        return JSON.toJSONString(result);
    }
    @RequestMapping("/add")
    public String add(){
        return "config/add";
    }

    @RequestMapping("/card/manage")
    public String card(ModelMap map){
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_CARD_INFO);
        CardInfo cardInfo = JSON.parseObject(s, CardInfo.class);
        map.put("card",cardInfo);
        String logoUrl = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_LOGO_URL);
        map.put("logoUrl",logoUrl);
        return "card/manage";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("id") Integer id, ModelMap map){
        BaseConfig baseConfig=baseConfigMapper.selectById(id);
        map.put("baseConfig",baseConfig);
        return "config/edit";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        BaseConfig baseConfig = new BaseConfig();
        baseConfig.setDateUpdate(new Date());
        baseConfig.setId(id);
        baseConfig.setArchive((byte)1);
        int row=baseConfigMapper.updateById(baseConfig);

        if(row != 0){
            stringRedisTemplate.delete(RedisConstant.DICT+baseConfigMapper.selectById(id).getName());
        }
        ResultHelper result= ResultHelper.wrapSuccessfulResult(null);
        return JSON.toJSONString(result);
    }


    @RequestMapping("/updateLogo")
    @ResponseBody
    public String updateLogo(@RequestParam("logoName") String logoName){
        Boolean flag = stringRedisTemplate.hasKey(RedisConstant.DICT_LOGO_URL);
        BaseConfig baseConfig=new BaseConfig();
        baseConfig.setName(RedisConstant.DICT_LOGO_URL);
        baseConfig.setValue(logoName);
        baseConfig.setDescription("网站logo地址");
        int row = 0 ;
        ResultHelper result=null;
        if(flag != null && flag){ //已经存在
            baseConfig.setDateUpdate(new Date());
            row = baseConfigMapper.updateByName(baseConfig);
        }
        if(row != 0){
            stringRedisTemplate.opsForValue().set(RedisConstant.DICT_LOGO_URL,baseConfig.getValue()+"?p=0");
            result=ResultHelper.wrapSuccessfulResult(null);
            return JSON.toJSONString(result);
        }
        result=ResultHelper.wrapErrorResult(1,"上传logo保存失败");
        return JSON.toJSONString(result);
    }

    @RequestMapping("/updateAvatar")
    @ResponseBody
    public String updateAvatar(@RequestParam("avatarName") String avatarName){

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_CARD_INFO);
        CardInfo cardInfo = JSON.parseObject(s, CardInfo.class);
        cardInfo.setAvatar(avatarName);
        stringRedisTemplate.opsForValue().set(RedisConstant.DICT_CARD_INFO,JSON.toJSONString(cardInfo));
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(null));
    }


    @RequestMapping("/updateCard")
    @ResponseBody
    public String update(CardInfo card){
        BaseConfig bc = new BaseConfig();
        bc.setName(RedisConstant.DICT_CARD_INFO);
        bc.setValue(JSON.toJSONString(card));
        bc.setDateInsert(new Date());
        bc.setDescription("个人信息");
        int save = baseConfigMapper.updateByName(bc);
        ResultHelper result;
        if(save != 0){
            stringRedisTemplate.opsForValue().set(RedisConstant.DICT_CARD_INFO,JSON.toJSONString(card));
            result = ResultHelper.wrapSuccessfulResult(null);
            return JSON.toJSONString(result);
        }
        result=ResultHelper.wrapErrorResult(1,"保存数据库失败");
        return JSON.toJSONString(result);
    }
}
