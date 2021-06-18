package com.syc.blog.entity.admin;


import java.io.Serializable;
import java.util.Date;

public class AdminRoleMenu implements Serializable {
    private Integer adminMenuId;
    private Integer adminRoleId;
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除

}
