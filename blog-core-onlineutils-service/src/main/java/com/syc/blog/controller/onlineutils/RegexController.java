package com.syc.blog.controller.onlineutils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.onlineutils.Regexp;
import com.syc.blog.mapper.onlineutils.RegexpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 正则表达式
 * */
@Controller
@RequestMapping("/util/regexp")
public class RegexController extends BaseController {

    @Autowired
    RegexpMapper regexpMapper;

    @RequestMapping("")
    public String regexp(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
    , @RequestParam("bindId") Integer bindId) {
        List<Regexp> list = regexpMapper.selectList(Wrappers.<Regexp>lambdaQuery().eq(Regexp::getArchive,0));
        map.put("list",list);
        byte type = 10;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/regexp";
    }


}
