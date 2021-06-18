package com.syc.blog.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleClassifyMapper extends BaseMapper<ArticleClassify> {
    @Select("select * from (select t1.id,t1.name,(select count(*) from article_classify t2 where t2.parent_id = " +
            "t1.id and t2.archive = 0 ) as directChildrenCount from article_classify t1  " +
            ") t where t.directChildrenCount = 0 order by rand() limit #{num} ")
    List<ArticleClassify> selectRandomList(@Param("num") int i);

    @Select("select u.*,(select count(*) from article_classify uu where uu.parent_id = u.id and uu.archive = 0) as directChildrenCount from article_classify u  where u.level = #{level} and u.archive = 0")
    List<ArticleClassify> selectListByLevel(@Param("level") int i);

    @Select("select u.*,(select count(*) from article_classify uu where uu.parent_id = u.id and uu.archive = 0) as directChildrenCount from article_classify u  where u.parent_id = #{parentId} and u.archive = 0")
    List<ArticleClassify> selectListByParentId(@Param("parentId") Integer id);

    @Select("SELECT " +
            "  GROUP_CONCAT(id ORDER BY id asc) " +
            "FROM " +
            "  ( " +
            "  SELECT " +
            "    @r AS _id, " +
            "    ( SELECT @r := parent_id FROM article_classify WHERE id = _id ) AS parent_id, " +
            "    @l := @l - 1 AS lvl  " +
            "  FROM " +
            "    ( SELECT @r := #{id}, @l := 3 ) vars, " +
            "    article_classify h  " +
            "  WHERE " +
            "    @r <> 0  " +
            "  ) T1 " +
            "  JOIN article_classify T2 ON T1._id = T2.id  " +
            "ORDER BY " +
            "  id ASC; " +
            "  ")
    String selectIdTree(@Param("id") Integer id);

    @Select("SELECT " +
            "  GROUP_CONCAT(name ORDER BY id asc) " +
            "FROM " +
            "  ( " +
            "  SELECT " +
            "    @r AS _id, " +
            "    ( SELECT @r := parent_id FROM article_classify WHERE id = _id ) AS parent_id, " +
            "    @l := @l - 1 AS lvl  " +
            "  FROM " +
            "    ( SELECT @r := #{id}, @l := 3 ) vars, " +
            "    article_classify h  " +
            "  WHERE " +
            "    @r <> 0  " +
            "  ) T1 " +
            "  JOIN article_classify T2 ON T1._id = T2.id  " +
            "ORDER BY " +
            "  id ASC; " +
            "  ")
    String selectNameTree(@Param("id") Integer id);
}
