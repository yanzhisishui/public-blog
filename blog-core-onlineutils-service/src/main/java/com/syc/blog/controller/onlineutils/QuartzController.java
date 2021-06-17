package com.syc.blog.controller.onlineutils;

import com.syc.blog.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * quartz表达式
 * */
@Controller
@RequestMapping("/util/quartz")
public class QuartzController extends BaseController {


    @RequestMapping("")
    public String quartz(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
    , @RequestParam("bindId") Integer bindId){
        byte type = 9;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/quartz";
    }
}
