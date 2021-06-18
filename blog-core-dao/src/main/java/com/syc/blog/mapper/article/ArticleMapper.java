package com.syc.blog.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syc.blog.entity.article.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<Article> queryListPage(IPage<Article> iPage, @Param("params") Map<String, Object> params);

    @Update("update article set browser  = #{browser} + 1  where id = #{id}")
    int increaseBrowser(Article article);

    int updateList(List<Article> list);
}
