package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.info.FriendLink;
import com.syc.blog.mapper.info.FriendLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkService {
    @Autowired
    FriendLinkMapper friendLinkMapper;
    public List<FriendLink> selectList() {
        return friendLinkMapper.selectList(Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getArchive,0));
    }
}
