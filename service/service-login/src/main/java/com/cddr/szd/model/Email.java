package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "email")
public class Email {
    // 发件人邮箱地址
    String from ;
    // 邮箱服务器地址
    String host;
    // 发件人邮箱用户名
    String username;
    // 发件人邮箱密码
    String password;
}
