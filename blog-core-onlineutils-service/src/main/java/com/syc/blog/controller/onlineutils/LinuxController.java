package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.onlineutils.Linux;
import com.syc.blog.mapper.onlineutils.LinuxMapper;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/util/linux")
public class LinuxController extends BaseController {

    @Autowired
    LinuxMapper linuxMapper;
    @RequestMapping("")
    public String linuxinstruction(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                   @RequestParam("bindId") Integer bindId){
        byte type = 6;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/linux";
    }


    @RequestMapping("/queryData")
    @ResponseBody
    public String queryData(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize,
                            @RequestParam(value = "type",required = false) String type,
                            @RequestParam(value = "value",required = false) String value){

        if(!StringHelper.isEmpty(value)){
            if(value.trim().length() < 2){
                ResultHelper result= ResultHelper.wrapErrorResult(1,"请输入大于两个字符");
                return JSON.toJSONString(result);
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("type",type);
        IPage<Linux> iPage = new Page<>(page,10);
        map.put("value",value);
        iPage = linuxMapper.selectByParams(iPage,map);
        ResultHelper<List<Linux>> result= ResultHelper.wrapSuccessfulResult(iPage.getRecords());
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getTotal")
    @ResponseBody
    public Integer getTotal(@RequestParam(value = "type",required = false) String type,
                                      @RequestParam(value = "value",required = false) String value){
        return linuxMapper.selectTotalCountLinux(type,value);
    }
}
