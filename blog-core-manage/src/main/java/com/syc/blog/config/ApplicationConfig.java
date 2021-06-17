package com.syc.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = ApplicationConfig.PREFIX)
public class ApplicationConfig {

    public static final String PREFIX = "application";

    private String zimgUploadUrl ;
    private String zimgAddressUrl ;
    private String videoUploadUrl ;
    private String videoAddressUrl ;

}
