package com.syc.blog.controller.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.config.AliYunOSSProperties;
import com.syc.blog.entity.file.FileCollection;
import com.syc.blog.mapper.file.FileCollectionMapper;
import com.syc.blog.utils.JsonHelper;

@Controller
@RequestMapping("/file/collection")
public class FileCollectionController {

    @Autowired
    FileCollectionMapper fileCollectionMapper;

    /**
     * 我的文件列表
     * */
    @GetMapping
    @ResponseBody
    public String list(@RequestParam("page") Integer page, @RequestParam("limit") Integer pageSize){
        IPage<FileCollection> data = new Page<>(page,pageSize);
        data = fileCollectionMapper.selectPage(data, null);
        return JsonHelper.objectToJsonForTable(data.getRecords(),data.getTotal());
    }

    @GetMapping("/manage")
    public String manage(){
        return "file/manage";
    }


    @GetMapping("/add")
    public String add(){
        return "file/add";
    }

    @Autowired
    AliYunOSSProperties aliYunOSSProperties;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".")+1);
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4G6CpwXXzqfxMa9F4rTp";
        String accessKeySecret = "NpzAg9tvJE7adUPrKF6Y2qGaErROFH";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String objectName = filename+suffix;
        InputStream inputStream = file.getInputStream();
        ossClient.putObject("sunyuchao-blog-file", objectName, inputStream);

        ossClient.shutdown();
        String src = aliYunOSSProperties.getUrlPrefix() + objectName;
        return JsonHelper.jsonForUpload(src);
    }
}
