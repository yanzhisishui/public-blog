package com.syc.blog.entity.file;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("file_collection")
public class FileCollection{
	@TableId(value = "id",type = IdType.AUTO)
	private  Integer  id;
	private  String  name;
	private  String  path;
	private Date createTime;
	private  Date  updateTime;

	@TableField(value = "is_deleted")
	@TableLogic
	private  Boolean  deleted;
}