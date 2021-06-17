package com.syc.blog.service.comment;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.mapper.comment.UserCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentService {

    @Autowired
    UserCommentMapper userCommentMapper;
    public List<UserComment> selectListLatest(int i) {
        return userCommentMapper.selectListLatest(i);
    }

    public IPage<UserComment> selectFirstLevelCommentPage(IPage<UserComment> page, Byte type, Integer bindId) {
        return userCommentMapper.selectFirstLevelCommentPage(page,type,bindId);
    }

    public List<UserComment> selectSecondLevelComment(Byte type, Integer bindId, Integer id) {
        return userCommentMapper.selectSecondLevelComment(type,bindId,id);
    }

    public int save(UserComment comment) {
        return userCommentMapper.insert(comment);
    }
}
