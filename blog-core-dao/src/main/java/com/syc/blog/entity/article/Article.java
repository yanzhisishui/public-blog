package com.syc.blog.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章
 * */
//@Document(indexName = "blog",type = "article")
@Data
@TableName("article")
@Document(indexName = "blog",type = "article")
public class Article implements Serializable {
    @Id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    @Transient
    private Date dateUpdate;
    @Transient
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
    private String description;//文章描述 用于首页展示，长度（250-270）
    private Integer classifyId;//类别ID
    private Integer browser;//浏览人数
    private Integer praise;//点赞人数
    private String content;//内容(样式、图片、小标题、HTML标签都包含在内)

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;//文章标题
    private String bread;
    @Transient
    private Integer userId;//发布者id
    private String imageUrl;//图片地址

    @TableField(exist = false)
    private ArticleClassify classify;

    @TableField(exist = false)
    @Transient
    private String classifyName;
    @TableField(exist = false)
    private Integer commentCount;
    @TableField(exist = false)
    private Integer collectionCount;

    @Override
    public boolean equals(Object  o){
        if(!(o instanceof Article)){
            return false;
        }
        Article o1 = (Article) o;
        return o1.getId().equals(this.getId());
    }
}
