package com.syc.blog.controller.onlineutils;

import com.syc.blog.constants.Constant;
import com.syc.blog.controller.BaseController;
import com.syc.blog.utils.ImageHelper;
import com.syc.blog.utils.JsonHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 图片压缩
 * */
@Controller
@RequestMapping("/util/imgPress")
public class ImagePressController extends BaseController {


    @RequestMapping("")
    public String imgPress(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                           @RequestParam("bindId") Integer bindId){
        byte type = 4;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/imgpress";
    }

    @RequestMapping("/uploadImg")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){
        String name = file.getOriginalFilename();
        String path= Constant.IMAGE_PRESS_PATH+name;
        try {
            OutputStream os=new FileOutputStream(path);
            FileCopyUtils.copy(file.getInputStream(),os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonHelper.jsonForUpload(path);
    }


    @RequestMapping("/press")
    public void press(@RequestParam(value = "targetWidth",required = false) Integer targetWidth,
                      @RequestParam("path") String path,
                      HttpServletResponse response) throws IOException {
        File press = ImageHelper.press(new File(path), targetWidth);
        response.setContentType("application/force-download");
        //设置文件名
        response.setHeader("Content-Disposition", "attachment;filename="+path.substring(path.lastIndexOf("/")));
        InputStream is=new FileInputStream(press);
        FileCopyUtils.copy(is,response.getOutputStream());
    }
}
