package com.syc.blog.utils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    private static Map<String,Integer>  keyMap = new HashMap<>();
    static {
        keyMap.put("CREATE",1);
        keyMap.put("TABLE",1);
        keyMap.put("NOT",1);
        keyMap.put("NULL",1);
        keyMap.put("AUTO_INCREMENT",1);
        keyMap.put("COLLATE",1);
        keyMap.put("PRIMARY",1);
        keyMap.put("KEY",1);
        keyMap.put("UNIQUE",1);
        keyMap.put("USING",1);
        keyMap.put("BTREE",1);
        keyMap.put("ENGINE",1);
        keyMap.put("InnoDB",1);
        keyMap.put("DEFAULT",1);
        keyMap.put("CHARSET",1);
        keyMap.put("utf8_unicode_ci",1);
        keyMap.put("UNSIGNED",1);
        keyMap.put("COMMENT",1);
        keyMap.put("SET",1);
        keyMap.put("INDEX",1);
        keyMap.put("ROW_FORMAT",1);
        keyMap.put("Compact",1);
        keyMap.put("FOREIGN_KEY_CHECKS",1);
        keyMap.put("CHARACTER",1);
        keyMap.put("REFERENCES",1);
        keyMap.put("FOREIGN",1);
        keyMap.put("CONSTRAINT",1);
        keyMap.put("ON",1);
        keyMap.put("DELETE",1);
        keyMap.put("RESTRICT",1);
        keyMap.put("UPDATE",1);
        keyMap.put("CASCADE",1);
    }

    public static String getEncodedStr(String origin, Charset byteEncode, Charset strEncode){
        byte[] bytes = origin.getBytes(byteEncode);
        return new String(bytes,strEncode);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 验证不合适的词语
     * */
    public static boolean hasIllegal(String content) {
        Set<String> list = IllegalWordsHelper.getIllegalWord(content, IllegalWordsHelper.maxMatchType);
        return list.size() != 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.trim().length() == 0;
    }


    private final static String[] AGENT = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" }; //定义移动端请求的所有可能类型
    /**
     * 判断User-Agent 是不是来自于手机
     * @param ua
     * @return
     */
    public static boolean checkAgentIsMobile(String ua) {
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : AGENT) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //判断是不是MySQL关键字
    public static boolean containsMySQLKeywords(String target) {
        return keyMap.containsKey(target);
    }

    //从mysql类型转换到Java实体类型
    public static String mysqlTypeToJava(String target) {
        if(target.contains("`")){ //防止字段里含有类型字符串
            return null;
        }
        target = target.toLowerCase();
        if(target.contains("bigint")){
            return "Long";
        }if(target.contains("tinyint")){
            return "Byte";
        } else if(target.contains("smallint")){
            return "Short";
        } else if(target.contains("int")){
            return "Integer";
        }else if(target.contains("varchar")){
            return "String";
        }else if(target.contains("char")){
            return "Integer";
        }else if(target.contains("datetime")){
            return "Date";
        }else if(target.contains("timestamp")){
            return "Long";
        }else if(target.contains("blob")){
            return "byte[]";
        }else if(target.contains("double")){
            return "Double";
        }else if(target.contains("text")){
            return "String";
        }else if(target.contains("bit")){
            return "Boolean";
        }else if(target.contains("decimal")){
            return "BigDecimal";
        }else if(target.contains("float")){
            return "Float";
        }else if(target.contains("long")){
            return "BigInt";
        }else if(target.contains("json")){
            return "String";
        }else if(target.contains("numeric")){
            return "BigDecimal";
        }else if(target.contains("binary")){
            return "byte[]";
        }
        return null;
    }

    //驼峰转换
    public static String toHump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //判断字符串是否有中文
    public static boolean containChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void main(String[] args) {
        System.out.println(keyMap.containsKey("CREATE"));
    }

    /**
     * 防止Xss攻击
     * */
    public static String filterXSS(String content) {
        content = content.replace("<","＜")
                .replace(">","＞")
                .replace("&lt;","＜")
                .replace("&gt;","＞")
                .replace("＜img","<img")
                .replace(".gif\"＞",".gif\">");
        return content;
    }
}
