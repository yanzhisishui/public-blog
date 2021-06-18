package com.syc.blog.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章分类
 * */

@Data
@TableName("article_classify")
public class ArticleClassify implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @Transient
    private Date dateInsert;
    @Transient
    private Date dateUpdate;
    @Transient
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
    private Integer parentId;//上级分类
    private String name;
    @Transient
    private Byte level;//类别级别

    @TableField(exist = false)
    private Integer directChildrenCount;//直接子分类个数
}
