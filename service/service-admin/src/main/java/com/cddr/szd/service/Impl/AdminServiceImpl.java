package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.helper.Md5Util;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.RegularUser;
import com.cddr.szd.model.User;
import com.cddr.szd.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addUser(RegularUser registerUser) {
        Permission.check(UserType.ADMIN.getCode());
        //校验验证码是否正确
        String s = stringRedisTemplate.opsForValue().get(registerUser.getEmail());
        if (s == null || s.isEmpty() || !s.equals(registerUser.getCaptcha())) {
            throw new BizException(BizCodeEnum.CODE_ERROR);
        }

        //判断当前邮箱或用户名是否被注册
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, registerUser.getEmail())
                .or()
                .eq(User::getName, registerUser.getName());
        User one = userMapper.selectOne(eq);
        if (one == null) {
            //对密码进行MD5加密
            String md5String = Md5Util.getMD5String("123456");
            //创建一个user对象
            User user = new User();
            //将前端传过来的合法对象赋值给user对象
            BeanUtils.copyProperties(registerUser, user);
            user.setType(UserType.EMPLOYEE.getCode()).setPassword(md5String)
                    .setAddPermission(0).setUpdataPermission(0).setDeletePermission(0).setSubmitExpiredItemPermission(0);
            //将user对象持久化到Mysql
            userMapper.insert(user);
        } else {
            throw new BizException(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }
}
