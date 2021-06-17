package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.syc.blog.controller.BaseController;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.utils.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则表达式
 * */
@Controller
@RequestMapping("/util/sqlToJava")
public class SqlToJavaController extends BaseController {

    @RequestMapping("")
    public String sqlToJava(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
    , @RequestParam("bindId") Integer bindId) {
        byte type = 13;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/sql_to_java";
    }

    @RequestMapping("/transfer")
    @ResponseBody
    public String transfer(@RequestParam("sql") String sql){
        ResultHelper result = null;
        /**
         * 分析字符串，词法分析器 ``
         * */
        if(!sql.contains("CREATE TABLE")|| !sql.contains("(")){
            return JSON.toJSONString(ResultHelper.wrapErrorResult(1,"请确定建表语句完整"));
        }
        StringBuilder lineResult  = new StringBuilder();
        sql = sql.replace("\n","");
        char[] arr = sql.toCharArray();
        StringBuilder sb = new StringBuilder();
        String target = "";
        List<String> list = new ArrayList<>();
        String className = "";

        for (char c : arr) {
            sb.append(c);
            if ((int) c == 32) { //空格
                target = sb.toString().replace(" ","");
                if(!StringHelper.containChinese(target) && !StringHelper.isEmpty(target)){
                    list.add(target);
                }
                //如果target不是MySQL关键字,并且不包含中文
                if (!StringHelper.containsMySQLKeywords(target) && !StringHelper.containChinese(target) && !StringHelper.isEmpty(target)) {
                    //判断是不是表名
                    if (list.size() > 1) {
                        String s = list.get(list.size() - 2);
                        if (s.equals("TABLE")) {
                            className = target.replace("`", ""); //类名
                            className = StringHelper.toHump(className);
                            className = className.substring(0, 1).toUpperCase() + className.substring(1);
                        }
                    }
                    //如果是MySQL类型,则获取对应的Java类型
                    String type = StringHelper.mysqlTypeToJava(target);
                    if (type != null) { //说明是mysql类型
                        //获取前一个字段
                        String prev = list.get(list.size() - 2).replace("`", "");//上一个单词(字段)
                        String field = StringHelper.toHump(prev);
                        lineResult.append("\t");
                        lineResult.append("private");
                        lineResult.append("  ");
                        lineResult.append(type);
                        lineResult.append("  ");
                        lineResult.append(field);
                        lineResult.append(";\n");
                    }
                }
                sb = new StringBuilder();
            }
        }
        String prefix  = "public class "+className+"{\n";
        String suffix = "}";
        result = ResultHelper.wrapSuccessfulResult(prefix+lineResult.toString()+suffix);
        return JSON.toJSONString(result);
    }

}
