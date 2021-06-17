package com.syc.blog.controller.onlineutils;

import com.syc.blog.controller.BaseController;
import com.syc.blog.utils.NumMoneyConvertHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/util/numMoneyConvert")
public class NumMoneyConvertController extends BaseController {


    @RequestMapping("")
    public String numMoneyConvert(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "bindId") Integer bindId){

        byte type = 15;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/nummoneyconvert";
    }

    @RequestMapping("/handle")
    @ResponseBody
    public String handle(@RequestParam("number") String number, @RequestParam("whole") Integer whole){
        String s = NumMoneyConvertHelper.toChinese(number, whole);
        return s;
    }
}
