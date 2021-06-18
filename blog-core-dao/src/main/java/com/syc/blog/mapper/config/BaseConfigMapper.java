package com.syc.blog.mapper.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.admin.AdminUser;
import com.syc.blog.entity.config.BaseConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BaseConfigMapper extends BaseMapper<BaseConfig> {
    @Update("update base_config set value = #{value},date_update = #{dateUpdate} where name = #{name}")
    int updateByName(BaseConfig baseConfig);

}
