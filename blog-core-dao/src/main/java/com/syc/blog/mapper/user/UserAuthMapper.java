package com.syc.blog.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.user.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
