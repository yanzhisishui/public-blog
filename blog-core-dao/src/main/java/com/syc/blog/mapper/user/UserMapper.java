package com.syc.blog.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.info.Banner;
import com.syc.blog.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
