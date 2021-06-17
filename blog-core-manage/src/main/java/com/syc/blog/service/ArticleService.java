package com.syc.blog.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.mapper.article.ArticleClassifyMapper;
import com.syc.blog.mapper.comment.UserCommentMapper;
import com.syc.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleClassifyMapper articleClassifyMapper;
    @Autowired
    UserCommentMapper userCommentMapper;
    public void syncToElasticsearch(Article article) {
        ArticleClassify articleClassify = articleClassifyMapper.selectById(article.getClassifyId());
        article.setClassify(articleClassify);
        Integer count = userCommentMapper.selectCount(Wrappers.<UserComment>lambdaQuery().eq(UserComment::getBindId, article.getId()).eq(UserComment::getType, 1));
        article.setCommentCount(count);
        article.setCollectionCount(0);
        articleRepository.save(article);
    }
}
