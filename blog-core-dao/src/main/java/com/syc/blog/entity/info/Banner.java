package com.syc.blog.entity.info;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("banner")
public class Banner implements Serializable {
    private Byte status;//状态 0：禁用 1：启用
    private String imageUrl;//图片地址
    private Integer sort;//排序
    private Integer articleId;
    private String title;
    private String content;
    private String value;//(可选值 one two three four five six)
    private String flag;//(可选值  LEFT,CENTER)

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
