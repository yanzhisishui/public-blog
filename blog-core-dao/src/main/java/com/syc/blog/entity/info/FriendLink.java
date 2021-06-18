package com.syc.blog.entity.info;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("friend_link")
public class FriendLink implements Serializable {
    private String title;
    private String url;
    private String description;


    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
