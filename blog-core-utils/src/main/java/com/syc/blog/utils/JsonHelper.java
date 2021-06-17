package com.syc.blog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    /**
     * @description 将对象转换为json格式的字符串
     * @author SYC
     * @return json格式的字符串
     * */
    public static String objectToJson(Object object){
        return JSON.toJSONString(object);
    }


    /**
     * @description 将一个map转为json格式的字符串，该方法用于数据table的接口返回
     * (该接口针对于 layui 框架的table接口)
     * @author SYC
     * @param list : 要显示在每一页表格里的数据集合
     * @param count : 数据库中的数据总数
     * @return json格式的字符串
     * */
    public static String objectToJsonForTable(List<?> list,Long count){
        Map<String,Object> map=new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", list);
        return JSON.toJSONString(map);
    }

    /**
     * @description 将一个json格式的字符串转换为指定类型的List集合
     * @param json : json格式的数据
     * @param clazz : 要转换的集合中对象的Class类型
     * @author SYC
     * */
    public static <T> List<T> jsonToObjList(String json,Class<T> clazz)
    {
        JSONArray objects = JSON.parseArray(json);//json字符串转成JSONArray
        return objects.toJavaList(clazz);
    }

    /**
     * @description 将json格式的字符串转换为一个实体对象
     * @param json : 要转换的字符串
     * @param clazz : 传入的Class泛型
     * @author SYC
     * */
    public static <T> T jsonToObj(String json,Class<T> clazz)
    {
        return JSON.parseObject(json, clazz);
    }

    /**
     * @description 返回一个固定格式的json，这是由于layui官方的强烈规定
     *              在使用一些接口的时候必须返回规定的格式
     * @param fileName : 文件名
     * @author SYC
     * */
    public static String jsonForUpload(String fileName) {
        String result="{\"code\" : 0,\"msg\" : \"\",\"data\" :{\"src\" : \""+fileName+"\"} }";
        return result;
    }

    public static String jsonForUploadError(String fileName) {
        String result="{\"code\" : 1,\"msg\" : \"上传失败\",\"data\" :{\"src\" : \""+fileName+"\"} }";
        return result;
    }


}
