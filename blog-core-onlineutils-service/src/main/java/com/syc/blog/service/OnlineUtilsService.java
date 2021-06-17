package com.syc.blog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.info.OnlineUtils;
import com.syc.blog.mapper.info.OnlineUtilsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineUtilsService {
    @Autowired
    OnlineUtilsMapper onlineUtilsMapper;
    public List<OnlineUtils> selectList() {
        return onlineUtilsMapper.selectList(
                Wrappers.<OnlineUtils>lambdaQuery().eq(OnlineUtils::getArchive,0)
        .orderByAsc(OnlineUtils::getSort));
    }
}
