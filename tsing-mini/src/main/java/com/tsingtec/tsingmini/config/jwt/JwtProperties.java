package com.tsingtec.tsingmini.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {
    //token过期时间
    Integer tokenExpireTime;
    //token加密密钥
    String secretKey;
}