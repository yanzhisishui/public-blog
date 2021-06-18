package com.syc.blog.entity.onlineutils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//省市区
@Data
//@TableName("provinces")
public class Provinces implements Serializable {
    private Integer parentId;
    private String name;
    private Integer level;
    private String code;
    private String childrenCode;//每个区县级别才有的字段，当前区县下所有镇的派出所编码，用“，”隔开

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
}
