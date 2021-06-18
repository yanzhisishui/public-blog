package com.syc.blog.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础信息表
 * */
@Data
@TableName("user")
public class User implements Serializable {

    private String nickname;
    private String avatar;

    private Byte status;//0：禁用 1:正常


    @TableField(exist = false)
     private String identityType;
    @TableField(exist = false)
     private String identifier;//登录标识
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
