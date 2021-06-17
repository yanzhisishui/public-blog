package com.syc.blog.controller.onlineutils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.onlineutils.Regexp;
import com.syc.blog.mapper.onlineutils.RegexpMapper;
import com.syc.blog.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 正则表达式
 * */
@Controller
@RequestMapping("/util/timestamp")
public class TimestampController extends BaseController {

    @RequestMapping("")
    public String timestamp(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
    , @RequestParam("bindId") Integer bindId) {
        byte type = 12;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/timestamp";
    }

    /**
     * @param type 0 : s
     * @param type 1 : ms
     * */
    @RequestMapping("/timestampTransferTime")
    @ResponseBody
    public String timestampTransferTime(@RequestParam("type") Byte type ,@RequestParam("timestamp") Long timestamp){
        if(type == 0){
            timestamp = timestamp * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date(timestamp));
        return format;
    }

    /**
     * @param type 0 : s
     * @param type 1 : ms
     * */
    @RequestMapping("/timeTransferTimestamp")
    @ResponseBody
    public String timeTransferTimestamp(@RequestParam("type") Byte type ,@RequestParam("time") String time){
        Date parse = null;
        try {
            parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return "请使用《yyyy-MM-dd HH:mm:ss》格式";
        }
        String result = "";
        if(type == 0){
           result = String.valueOf(parse.getTime()/1000);
        }else{
            result = String.valueOf(parse.getTime() );
        }
        return result;
    }

    public static void main(String[] args) {
        Long s =1592968185L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date(s * 1000));
        System.out.println(format);
    }
}
