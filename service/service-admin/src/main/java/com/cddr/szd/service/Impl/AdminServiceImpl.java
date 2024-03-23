package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.helper.Md5Util;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.RegularUser;
import com.cddr.szd.model.SearchUser;
import com.cddr.szd.model.User;
import com.cddr.szd.model.UserPermission;
import com.cddr.szd.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            throw new  BizException(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    @Override
    public void updateUser(RegularUser regularUser) {
        Permission.check(UserType.ADMIN.getCode());
        //校验验证码是否正确
        String s = stringRedisTemplate.opsForValue().get(regularUser.getEmail());
        //校验修改的数据是否合法
        ParamValida(regularUser);
        if (s == null || s.isEmpty() || !s.equals(regularUser.getCaptcha())) {
            throw new BizException(BizCodeEnum.CODE_ERROR);
        }
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                .eq(User::getId, regularUser.getId());
        User one = userMapper.selectOne(eq);
        if (one == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (one.getType() != UserType.EMPLOYEE.getCode()){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        one.setName(regularUser.getName()).setEmail(regularUser.getEmail()).setCompanyName(regularUser.getCompanyName());
        int i = userMapper.updateById(one);
        if (i != 1) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        Permission.check(UserType.ADMIN.getCode());
        if (id == null || id < 1) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        User user = userMapper.selectById(id);
        if (user == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (user.getType() != UserType.EMPLOYEE.getCode()){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        int i = userMapper.deleteById(id);
        if (i != 1) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public IPage<User> findUser(SearchUser searchUser) {
        Permission.check(UserType.ADMIN.getCode());
        Page<User> userInfoPage = new Page<>(searchUser.getPageNum(), searchUser.getPageSize());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(searchUser.getName())) {
            userLambdaQueryWrapper = userLambdaQueryWrapper.like(User::getName, searchUser.getName());
        }
        if (!StringUtils.isEmpty(searchUser.getEmail())){
            userLambdaQueryWrapper = userLambdaQueryWrapper.like(User::getEmail, searchUser.getEmail());
        }
        userLambdaQueryWrapper.eq(User::getType, UserType.EMPLOYEE.getCode());
        userInfoPage = userMapper.selectPage(userInfoPage, userLambdaQueryWrapper);
        return userInfoPage;
    }
    @Override
    public void updatePermission(UserPermission userPermission) {
        Permission.check(UserType.ADMIN.getCode());
        if (userPermission.getId() == null || userPermission.getId() < 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        User user = userMapper.selectById(userPermission.getId());
        if (user == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        user.setAddPermission(userPermission.getAddPermission()).setUpdataPermission(userPermission.getUpdataPermission())
                .setDeletePermission(userPermission.getDeletePermission()).setSubmitExpiredItemPermission(userPermission.getSubmitExpiredItemPermission());
        int i = userMapper.updateById(user);
        if (i != 1) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public void resetPassword(Integer id) {
        Permission.check(UserType.ADMIN.getCode());
        if (id == null || id < 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        User user = userMapper.selectById(id);
        if (!user.getType().equals(UserType.EMPLOYEE.getCode())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        user.setPassword(Md5Util.getMD5String("123456"));
        int i = userMapper.updateById(user);
        if (i != 1) {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }


    private void ParamValida  (RegularUser regularUser){
        LambdaQueryWrapper<User> name = new LambdaQueryWrapper<User>().eq(User::getName, regularUser.getName());
        User userName = userMapper.selectOne(name);
        if (userName!= null && !userName.getId().equals(regularUser.getId())){
            throw new BizException(BizCodeEnum.USERNAME_EXIST);
        }
        LambdaQueryWrapper<User> email = new LambdaQueryWrapper<User>().eq(User::getEmail, regularUser.getEmail());
        User userEmail = userMapper.selectOne(email);
        if (userEmail!= null && !userEmail.getId().equals(regularUser.getId())){
            throw new BizException(BizCodeEnum.EMAIL_EXIST);
        }
    }

}
