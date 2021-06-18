package com.syc.blog.mapper.onlineutils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.onlineutils.HttpCode;
import com.syc.blog.entity.onlineutils.Regexp;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegexpMapper extends BaseMapper<Regexp> {
}
