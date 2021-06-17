package com.syc.blog.service.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.constants.Constant;
import com.syc.blog.entity.config.BaseConfig;
import com.syc.blog.mapper.config.BaseConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseConfigService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    BaseConfigMapper baseConfigMapper;
    public void initRedis() {
        List<BaseConfig> baseConfigs = baseConfigMapper.selectList(Wrappers.<BaseConfig>lambdaQuery().eq(BaseConfig::getArchive, 0));
        for(BaseConfig bc : baseConfigs){
            String name = bc.getName();
            Boolean flag = stringRedisTemplate.hasKey(name);
            if(flag == null || !flag){
                stringRedisTemplate.opsForValue().set(name,bc.getValue());
            }
        }
    }
}
