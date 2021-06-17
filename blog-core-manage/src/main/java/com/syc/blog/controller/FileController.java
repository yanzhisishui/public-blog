package com.syc.blog.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.config.ApplicationConfig;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.config.BaseConfig;
import com.syc.blog.mapper.config.BaseConfigMapper;
import com.syc.blog.utils.JsonHelper;
import com.syc.blog.utils.StringHelper;
import com.syc.blog.utils.ZimgUploadHelper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    BaseConfigMapper baseConfigMapper;

    @RequestMapping("/uploadToZimg")
    @ResponseBody
    public String uploadToZimg(@RequestParam("file") MultipartFile file){
        String address = ZimgUploadHelper.uploadImageToZimg(file,applicationConfig.getZimgUploadUrl(),applicationConfig.getZimgAddressUrl(),false);
        return JsonHelper.jsonForUpload(address);
    }


    @RequestMapping("/uploadToZimgResource")
    @ResponseBody
    public String uploadToZimgResource(@RequestParam("file") MultipartFile file){
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.DICT_NEED_WATER_MARK);
        if(s == null){
            s = baseConfigMapper.selectOne(Wrappers.<BaseConfig>lambdaQuery().eq(BaseConfig::getName,RedisConstant.DICT_NEED_WATER_MARK)).getValue();
            stringRedisTemplate.opsForValue().set(RedisConstant.DICT_NEED_WATER_MARK,s);
        }
        boolean needWaterMark = Boolean.parseBoolean(s);

        String address = ZimgUploadHelper.uploadImageToZimgResource(file,applicationConfig.getZimgUploadUrl(),applicationConfig.getZimgAddressUrl(),needWaterMark);
        return JsonHelper.jsonForUpload(address);
    }

    /**
     * 上传视频
     * */
    @RequestMapping("/uploadVideo")
    @ResponseBody
    public String uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));//拿到原始文件格式
        String uuid = StringHelper.getUUID();
        File path = new File(applicationConfig.getVideoUploadUrl());
        if(!path.exists()){
            path.mkdirs();
        }
        File f = new File(applicationConfig.getVideoUploadUrl()+File.separator+uuid + suffix);
        FileOutputStream fos = new FileOutputStream(f);
        FileCopyUtils.copy(is,fos);
        return JsonHelper.jsonForUpload(applicationConfig.getVideoAddressUrl()+uuid+suffix);
    }


    /**
     * 本地才用的到
     * */
    @RequestMapping("/files/video/{uuid}")
    public void test2(HttpServletResponse response, @PathVariable("uuid") String uuid) throws IOException {
        File file = new File(applicationConfig.getVideoUploadUrl()+File.separator+uuid);
        FileInputStream in = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        byte[] b = null;
        while(in.available() >0) {
            if(in.available()>10240) {
                b = new byte[10240];
            }else {
                b = new byte[in.available()];
            }
            in.read(b, 0, b.length);
            out.write(b, 0, b.length);
        }
        in.close();
        out.flush();
        out.close();
    }
}
