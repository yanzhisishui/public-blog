package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.info.MicroDiary;
import com.syc.blog.mapper.info.MicroDiaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicroDiaryService {
    @Autowired
    MicroDiaryMapper microDiaryMapper;
    public IPage<MicroDiary> selectListPage(IPage<MicroDiary> page) {
        return microDiaryMapper.selectPage(page, Wrappers.<MicroDiary>lambdaQuery().eq(MicroDiary::getArchive,0).orderByDesc(MicroDiary::getDateInsert));
    }
}
