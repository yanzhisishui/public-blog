package com.syc.blog.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户授权信息表
 * */
@Data
@TableName("user_auth")
public class UserAuth implements Serializable {
    private Integer userId;
    private String identityType;//登录类型：手机号  邮箱，qq等
    private String identifier;//标识:手机号 邮箱 用户名或第三方应用的唯一标识
    private String credential ;//密码凭证

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
