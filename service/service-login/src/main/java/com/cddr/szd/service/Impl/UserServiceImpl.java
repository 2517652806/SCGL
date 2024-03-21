package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.helper.JWTHelper;
import com.cddr.szd.helper.Md5Util;
import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.loginModel.RegularUser;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.Email;
import com.cddr.szd.model.User;
import com.cddr.szd.model.vo.UserVo;
import com.cddr.szd.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private Email email;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    /**
     * 发送验证码
     *
     * @param emailNum
     */
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
            throw new BizException(BizCodeEnum.CODE_ERROR);
        }
    }

    /**
     * 家庭版用户注册
     *
     * @param registerUser
     */
    @Override
    public void register(RegularUser registerUser, Integer code) {
        //校验两次密码是否一致
        if (!registerUser.getPassword().equals(registerUser.getConfirmPassword())) {
            throw new BizException(BizCodeEnum.PASSWORD_IS_NOT_TRUE);
        }
        //校验验证码是否正确
        String s = stringRedisTemplate.opsForValue().get(registerUser.getEmail());
        if (s == null || s.isEmpty() || !s.equals(registerUser.getCaptcha())) {
            throw new BizException(BizCodeEnum.CODE_ERROR);
        }

        //判断当前邮箱和用户名是否被注册
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, registerUser.getEmail())
                .or()
                .eq(User::getName, registerUser.getName());
        User one = userMapper.selectOne(eq);
        if (one == null) {
            //对密码进行MD5加密
            String md5String = Md5Util.getMD5String(registerUser.getPassword());
            registerUser.setPassword(md5String);
            //创建一个user对象
            User user = new User();
            //将前端传过来的合法对象赋值给user对象
            BeanUtils.copyProperties(registerUser, user);
            if (code.equals(UserType.USER.getCode())) {
                user.setType(code)
                        .setAddPermission(1).setUpdataPermission(1).setDeletePermission(1).setSubmitExpiredItemPermission(1);
            }else {
                user.setType(code)
                        .setAddPermission(0).setUpdataPermission(0).setDeletePermission(0).setSubmitExpiredItemPermission(0);
            }
            //将user对象持久化到Mysql
            userMapper.insert(user);
        } else {
            throw new BizException(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }


    @Override
    public void updatePassword(String captcha, String newPwd, String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new BizException(BizCodeEnum.PASSWORD_IS_NOT_TRUE);
        }
        String token = ThreadLocalUtil.get();
        if (token == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        User userInfo = JWTHelper.getUserInfo(token);
        //校验验证码是否正确
        String s = stringRedisTemplate.opsForValue().get(userInfo.getEmail());
        if (s == null || s.isEmpty() || !s.equals(captcha)) {
            throw new BizException(BizCodeEnum.CODE_ERROR);
        }
        String md5String = Md5Util.getMD5String(newPwd);
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>().eq(User::getId, userInfo.getId());
        User user = userMapper.selectOne(eq);
        user.setPassword(md5String);
        int i = userMapper.updateById(user);
        if (i == 1) {
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            stringStringValueOperations.getOperations().delete(user.getId().toString());
        }
    }

    @Override
    public void logout() {
        String token = ThreadLocalUtil.get();
        if (token == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        User userInfo = JWTHelper.getUserInfo(token);
        Boolean delete = stringRedisTemplate.delete(userInfo.getId().toString());
        if (!delete) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        ThreadLocalUtil.remove();
    }

    @Override
    public String login(UserVo userVo, Integer code) {
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                .eq(User::getName, userVo.getUserName())
                .eq(User::getType,code);
        User user = userMapper.selectOne(eq);
        if (user == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (!Md5Util.checkPassword(userVo.getPassword(), user.getPassword())) {
            throw new BizException(BizCodeEnum.PASSWORD_NOT_TRUE);
        }

        String token = JWTHelper.createToken(user);
        //将token存入redis
        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        forValue.set(user.getName(), token, 1, TimeUnit.HOURS);
        return token;
    }
}
