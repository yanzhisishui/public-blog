package com.syc.blog.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_praise_article")
public class UserPraiseArticle implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private Byte status;
    private Byte archive;
    private Date dateInsert;
    private Date dateUpdate;
}
