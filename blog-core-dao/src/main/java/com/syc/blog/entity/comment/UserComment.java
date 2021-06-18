package com.syc.blog.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 人与人评论
 * */
@Data
@TableName("user_comment")
public class UserComment implements Serializable {
    private Integer userId;//评论者
    private Integer commentedUserId;//被评论者
    private String content;//内容
    private Byte level;//1级回复还是二级回复
    private Byte type; //1：给我留 2：文章留  3：通知留
    private Integer bindId; //文章id或者通知id

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Date dateInsert;
    private Date dateUpdate;
    private Byte archive;//删除标志 0:未删除  1：逻辑删除
    private Integer parentId;

    @TableField(exist = false)
    private Integer praiseCount;//赞
    @TableField(exist = false)
    private Integer unPraiseCount;//踩
    //评论者
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String identityType;//用户类型
    //被评论者
    @TableField(exist = false)
    private String commentedUserName;
    @TableField(exist = false)
    private String commentedAvatar;

    @TableField(exist = false)
    private List<UserComment> childrenList;


    /**
     * 子回复模板
     * */
    public static class ChildrenMessageTemplate {
        private Integer userId;//回复者id
        private String avatar;//回复者头像
        private String commentUsername;//回复者昵称
        private String commentedUsername;//被回复者昵称
        private String time;//时间
        private String content;//回复内容

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCommentUsername() {
            return commentUsername;
        }

        public void setCommentUsername(String commentUsername) {
            this.commentUsername = commentUsername;
        }

        public String getCommentedUsername() {
            return commentedUsername;
        }

        public void setCommentedUsername(String commentedUsername) {
            this.commentedUsername = commentedUsername;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 一级回复模板
     * */
    public static class FirstMessageTemplate{
        private String avatar;
        private String commentUsername;//昵称
        private String time;//
        private String content;//
        private String commentUsernameCopy;//昵称（和commentUsername一样）
        private Integer commentId;//回复者id

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCommentUsername() {
            return commentUsername;
        }

        public void setCommentUsername(String commentUsername) {
            this.commentUsername = commentUsername;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentUsernameCopy() {
            return commentUsernameCopy;
        }

        public void setCommentUsernameCopy(String commentUsernameCopy) {
            this.commentUsernameCopy = commentUsernameCopy;
        }

        public Integer getCommentId() {
            return commentId;
        }

        public void setCommentId(Integer commentId) {
            this.commentId = commentId;
        }
    }
}
