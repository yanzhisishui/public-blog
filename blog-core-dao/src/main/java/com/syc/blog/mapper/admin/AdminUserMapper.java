package com.syc.blog.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.admin.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
