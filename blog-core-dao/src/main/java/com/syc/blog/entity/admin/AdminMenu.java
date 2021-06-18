package com.syc.blog.entity.admin;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AdminMenu implements Serializable {
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
    private String name;
    private String parentName;
    private String url;
    private String icon;
    private Integer parentId;
    private Byte level;//菜单等级
    private Integer roleId;//对应的权限id



}
