package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    public List<OnlineUtils> selectListLatest(int i) {
        QueryWrapper<OnlineUtils> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("archive",0);
        queryWrapper.eq("status",1);
        queryWrapper.orderByDesc("date_insert");
        String sql = "limit "+5;
        queryWrapper.last(sql);
        return onlineUtilsMapper.selectList(queryWrapper);
    }
}
