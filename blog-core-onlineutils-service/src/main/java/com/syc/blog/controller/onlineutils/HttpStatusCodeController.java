/*
package com.syc.blog.controller.onlineutils;

import com.syc.blog.annonation.BlockMaliciousAccess;
import com.syc.blog.controller.BaseController;
import com.syc.blog.service.common.IUserCommentService;
import com.syc.blog.service.onlineutils.IHttpStatusCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/util/httpStatusCode")
public class HttpStatusCodeController extends BaseController {

    @Autowired
    IHttpStatusCodeService httpStatusCodeService;
    @Autowired
    IUserCommentService userCommentService;

    @RequestMapping("")
    public String httpStatusCode(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                 @RequestParam("bindId") Integer bindId){
        putPageConst(map);
        //从数据库取数据
        byte type = 3;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/httpcodestatus";
    }


}
*/
