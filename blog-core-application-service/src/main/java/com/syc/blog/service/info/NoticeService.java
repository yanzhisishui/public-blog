package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syc.blog.entity.info.Notice;
import com.syc.blog.mapper.info.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    public List<Notice> selectListLatest(int i) {
        QueryWrapper<Notice> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("archive",0);
        queryWrapper.orderByDesc("date_insert");
        String sql = "limit "+i;
        queryWrapper.last(sql);
        return noticeMapper.selectList(queryWrapper);
    }

    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    public List<Notice> selectNextAndPrev(Integer id) {
        return noticeMapper.selectNextAndPrev(id);
    }
}
