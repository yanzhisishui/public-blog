package com.syc.blog.controller.onlineutils;

import com.syc.blog.constants.Constant;
import com.syc.blog.constants.GlobalConstant;
import com.syc.blog.controller.BaseController;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.QRCodeHelper;
import com.syc.blog.utils.StringHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/util/qrCode")
public class QRCodeController extends BaseController {



    @RequestMapping("")
    public String qrcode(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                         @RequestParam(value = "bindId") Integer bindId){
        byte type = 8;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/qrcode";
    }

    /**
     * 生成二维码
     * */
    @RequestMapping(value = "/createQRCodeImg",produces = "image/jpeg")
    public void createVCodeImg(@RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "content",required = false) String content,
                               @RequestParam(value = "width",required = false) Integer width,
                               @RequestParam(value = "logoPath",required = false) String logoPath,
                               HttpServletResponse response){
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            if(type != null){
                ClassPathResource resource=new ClassPathResource("favicon.ico");
                QRCodeHelper.createQRCode(300,300, GlobalConstant.HOST_ADDRESS,resource.getInputStream(),response);
            } else {
                InputStream in = StringHelper.isEmpty(logoPath) ? null : new FileInputStream(logoPath);
                QRCodeHelper.createQRCode(width,width,content,in,response);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    @RequestMapping("/uploadLogo")
    @ResponseBody
    public String uploadLogo(@RequestParam("file") MultipartFile file){
        String name = file.getOriginalFilename();//文件名
        String logoPath = Constant.QRCODE_LOGO_PATH+name;
        File f=new File(logoPath);
        try {
            OutputStream os=new FileOutputStream(f);
            FileCopyUtils.copy(file.getInputStream(),os);
        } catch (IOException e) {
           e.printStackTrace();
        }
        return JsonHelper.jsonForUpload(logoPath);
    }
}
