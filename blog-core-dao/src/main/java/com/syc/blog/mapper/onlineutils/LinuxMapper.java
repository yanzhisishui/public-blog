package com.syc.blog.mapper.onlineutils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syc.blog.entity.onlineutils.HttpCode;
import com.syc.blog.entity.onlineutils.Linux;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface LinuxMapper extends BaseMapper<Linux> {

    IPage<Linux> selectByParams(IPage<Linux> iPage,@Param("params") Map<String, Object> map);


    Integer selectTotalCountLinux(@Param("type") String type, @Param("value") String value);
}
