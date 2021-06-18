package com.syc.blog.mapper.info;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.info.Banner;
import com.syc.blog.entity.info.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select id,title from notice where id in (#{id} - 1,#{id} + 1) and archive = 0")
    List<Notice> selectNextAndPrev(@Param("id") Integer id);
}
