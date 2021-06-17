package com.syc.blog.controller.onlineutils;

import com.syc.blog.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * json 格式化
 * */
@Controller
@RequestMapping("/util/jsonFormat")
public class JsonFormatController extends BaseController {

    @RequestMapping("")
    public String jsonformat(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                             @RequestParam("bindId") Integer bindId){
        byte type = 5;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/jsonformat";
    }
}
