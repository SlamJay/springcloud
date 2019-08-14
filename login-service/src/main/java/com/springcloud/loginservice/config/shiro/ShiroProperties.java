package com.springcloud.loginservice.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.shiro")
public class ShiroProperties {

    private String loginUrl;
    private String logoutUrl;
    private String anonUrl;
}
