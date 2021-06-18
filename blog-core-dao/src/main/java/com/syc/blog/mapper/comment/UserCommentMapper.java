package com.syc.blog.mapper.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syc.blog.entity.comment.UserComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {
    @Select(" SELECT t1.user_id,t2.nickname as username,t2.avatar,t1.content,t1.date_insert," +
            "t3.identity_type as identityType FROM `user_comment` t1 " +
            "INNER JOIN user t2 on t1.user_id = t2.id inner join user_auth " +
            "t3 on t2.id=t3.user_id  ORDER BY t1.date_insert desc limit #{pageSize}")
    List<UserComment> selectListLatest(@Param("pageSize") int i);

    IPage<UserComment> selectFirstLevelCommentPage(IPage<UserComment> page,@Param("type") Byte type,@Param("bindId") Integer bindId);

    List<UserComment> selectSecondLevelComment(@Param("type") Byte type, @Param("bindId") Integer bindId, @Param("parentId") Integer id);
}
