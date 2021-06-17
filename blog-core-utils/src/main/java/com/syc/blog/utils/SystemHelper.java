package com.syc.blog.utils;

import java.util.Properties;

public class SystemHelper {
    public static String getOperateSystem(){
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name").toLowerCase(); //操作系统名称
        return  osName.contains("windows") ? "WINDOWS" : "LINUX";
    }
}
