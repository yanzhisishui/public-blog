package com.syc.blog.mapper.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.feedback.FeedBack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeedBackMapper extends BaseMapper<FeedBack> {
    @Select("SELECT count(*) FROM `feed_back`  where TO_DAYS(date_insert) = TO_DAYS(now()) and user_id = #{userId}")
    Integer selectTodayCountByUserId(@Param("userId") Integer userId);
}
