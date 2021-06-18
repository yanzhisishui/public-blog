package com.syc.blog.entity.info;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线工具
 * */
@Data
@TableName("online_utils")
public class OnlineUtils implements Serializable {
    private String title;
    private String description;
    private String icon;//图标class
    private String url;//跳转地址
    private Byte sort;
    private Byte status;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
