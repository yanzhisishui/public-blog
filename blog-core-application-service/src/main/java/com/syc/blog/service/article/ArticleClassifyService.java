package com.syc.blog.service.article;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.mapper.article.ArticleClassifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleClassifyService {
    @Autowired
    ArticleClassifyMapper articleClassifyMapper;

    public List<ArticleClassify> selectListByLevel(int i) {

        return articleClassifyMapper.selectListByLevel(i);
    }

    public List<ArticleClassify> selectRandomList(int i) {
        return articleClassifyMapper.selectRandomList(i);
    }

    public List<ArticleClassify> selectHotTagList() {
        return articleClassifyMapper.selectList(Wrappers.<ArticleClassify>lambdaQuery().eq(ArticleClassify::getArchive,0).eq(ArticleClassify::getLevel,3));
    }

    public List<ArticleClassify> selectListByParentId(Integer id) {
        return articleClassifyMapper.selectListByParentId(id);
    }

    public String selectIdTree(Integer id) {
        return articleClassifyMapper.selectIdTree(id);
    }
}
