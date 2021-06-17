package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.info.Skill;
import com.syc.blog.mapper.info.SkillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    SkillMapper skillMapper;
    public List<Skill> selectList() {

        return skillMapper.selectList(Wrappers.<Skill>lambdaQuery().eq(Skill::getArchive,0).eq(Skill::getStatus,1));
    }
}
