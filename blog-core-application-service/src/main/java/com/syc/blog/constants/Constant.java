package com.syc.blog.constants;

import com.alibaba.fastjson.JSON;
import com.syc.blog.entity.user.CardInfo;

public class Constant {

    public static final String BLOG_INTRODUCTION="96年草根站长，2019年接触互联网踏入Java开发岗位，喜欢前后端技术。对技术有强烈的渴望，" +
            "2019年11月正式上线自己的个人博客";
    public static final String BLOG_ADDRESS_NOW="江苏省南京市";
    public static final String BLOG_EMAIL = "1481232419@qq.com" ;
    public static final String BLOG_HOBBY = "电子竞技、散步、钓鱼、学习" ;
    public static final String BLOG_NICKNAME = "暮色妖娆丶" ;
    public static final String BLOG_PROFESSIONAL = "Java开发工程师" ;
    public static final String BLOG_AVATAR = "http://www.sunyuchao.com/files/img/b58527d6c85beaf0e41af5f6903dee77?p=0" ;
    public static final String DICT = "dict:" ;
    public static final String NEW_YEAR_DATE=DICT+"new_year_date";
    public static final String DICT_LOGO_URL = DICT+"logo_url";
    public static final String DICT_ICONFONT_URL = DICT+"iconfont_url";
    public static final String HOW_BUILD_BLOG_DESC = DICT+"how_build_blog_desc";


    /**
     * rabbitmq
     * */

    public static final String RABBITMQ_EXCHANGE = "application-service.browser";
    public static final String RABBITMQ_QUEUE = "application-service.browser.count";
    /**
     * 评论类型
     * */
    public static final Byte USER_COMMENT_TYPE_ARTICLE = 1;
    public static final Byte USER_COMMENT_TYPE_NOTICE = 2;
    public static final Byte USER_COMMENT_TYPE_UTIL_HTTPCODE = 3;
    public static final Byte USER_COMMENT_TYPE_UTIL_IMAGE_PRESS= 4;
    public static final Byte USER_COMMENT_TYPE_UTIL_JSON_FORMAT= 5;
    public static final Byte USER_COMMENT_TYPE_UTIL_LINUX= 6;
    public static final Byte USER_COMMENT_TYPE_UTIL_MONEY_CONVERT= 7;
    public static final Byte USER_COMMENT_TYPE_UTIL_QRCODE= 8;
    public static final Byte USER_COMMENT_TYPE_UTIL_QUARTZ= 9;
    public static final Byte USER_COMMENT_TYPE_UTIL_REGEXP= 10;
    public static final Byte USER_COMMENT_TYPE_UTIL_IDCARD= 11;
    public static final String LOGIN_EMAIL = "EMAIL";
    public static final String LOGIN_QQ =  "QQ";
    public static final String SMS_EMAIL = "sms:email:";
    public static final String SMS_COUNT = "sms:count:";
    public static final String USER_LOGIN_SESSION_KEY = "loginUser";


    public static void main(String[] args) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setNickname(Constant.BLOG_NICKNAME);
        cardInfo.setAddressNow(Constant.BLOG_ADDRESS_NOW);
        cardInfo.setProfession(Constant.BLOG_PROFESSIONAL);
        cardInfo.setEmail(Constant.BLOG_EMAIL);
        cardInfo.setHobby(Constant.BLOG_HOBBY);
        cardInfo.setAvatar(Constant.BLOG_AVATAR);
        cardInfo.setIntroduction(Constant.BLOG_INTRODUCTION);
        String s = JSON.toJSONString(cardInfo);
        System.out.println(s);

        System.out.println(System.currentTimeMillis()/1000);
    }
}
