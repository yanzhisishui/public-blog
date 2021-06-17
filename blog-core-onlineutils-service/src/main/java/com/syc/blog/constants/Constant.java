package com.syc.blog.constants;

import com.syc.blog.utils.SystemHelper;

public class Constant {

    public static final String IMAGE_PRESS_PATH= SystemHelper.getOperateSystem().equals("WINDOWS") ?
            "D:/test/" : "/data/imgpress/";
    public static final String QRCODE_LOGO_PATH=SystemHelper.getOperateSystem().equals("WINDOWS") ?
            "D:/test/" : "/data/qrcode/";

    public static final String USER_LOGIN_SESSION_KEY = "loginUser";
}