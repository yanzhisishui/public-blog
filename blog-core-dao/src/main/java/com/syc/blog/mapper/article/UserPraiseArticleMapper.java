package com.syc.blog.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.UserPraiseArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserPraiseArticleMapper extends BaseMapper<UserPraiseArticle> {
    int saveList(List<UserPraiseArticle> list);

}
