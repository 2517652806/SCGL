package com.cddr.szd.service.Impl;

import com.cddr.szd.exception.SZDException;
import com.cddr.szd.model.Email;
import com.cddr.szd.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private Email email;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    @Async//发送邮件会阻塞主线程 则使用Async来异步发送邮件
    public void getCaptcha(String emailNum) {
        Random random = new Random();
        //生成6位验证码
        String verificationCode = String.valueOf(random.nextInt(999999 - 100000 + 1) + 100000);
        // 设置邮件属性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", email.getHost());
        properties.put("mail.smtp.auth", "true");
        // 获取默认Session对象
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getUsername(), email.getPassword());
            }
        });
        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // 设置 From: 头部头字段
            message.setFrom(new InternetAddress(email.getFrom()));
            // 设置 To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailNum));
            // 设置主题
            message.setSubject("验证码");
            // 设置消息体
            message.setText("您的验证码是：" + verificationCode + "有效期为5分钟");
            // 发送消息
            Transport.send(message);
            //邮件发送成功后，将验证码存入redis key为邮箱号 过期时间为5分钟
            stringRedisTemplate.opsForValue().set(emailNum, verificationCode, 5, TimeUnit.MINUTES);
        } catch (MessagingException e) {
            throw new SZDException("验证码获取失败",100);
        }
    }
}
