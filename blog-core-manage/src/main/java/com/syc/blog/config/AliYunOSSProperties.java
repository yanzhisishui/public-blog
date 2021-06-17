package com.syc.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix= AliYunOSSProperties.PREFIX )
@Data
@Validated
public class AliYunOSSProperties {
 /**
  * 阿里OSS服务配置前缀名
  *
  */
 public static final String PREFIX="aliyun.oss";

 private String endpoint ;


 private String accessKeyId;

 private String accessKeySecret;
 private String buckName;
 private String urlPrefix;



}