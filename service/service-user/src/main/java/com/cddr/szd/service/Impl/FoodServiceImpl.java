package com.cddr.szd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.ExpiredStatusEnum;
import com.cddr.szd.enums.PermissionEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.helper.JWTHelper;
import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.mapper.FoodTypeMapper;
import com.cddr.szd.mapper.FoodUserMapper;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.*;
import com.cddr.szd.service.FoodService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodUserMapper foodUserMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FoodTypeMapper foodTypeMapper;
    @Override
    public void purchaseFood(FoodVo foodVo) {
        Permission.check(UserType.USER.getCode());
        User user = permissionVerify(PermissionEnum.ADD_PERMISSION.getCode());
        FoodUser foodUser = new FoodUser();
        BeanUtils.copyProperties(foodVo, foodUser);
        foodUser.setUserId(user.getId());
        int insert = foodUserMapper.insert(foodUser);
        if (insert != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }


    @Override
    public void updateFood(FoodVo foodVo) {
        Permission.check(UserType.USER.getCode());
        User user = permissionVerify(PermissionEnum.UPDATE_PERMISSION.getCode());
        FoodUser foodUser = foodUserMapper.selectById(foodVo.getId());
        if (foodUser == null|| !foodUser.getUserId().equals(user.getId())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        BeanUtils.copyProperties(foodVo, foodUser);
        int i = foodUserMapper.updateById(foodUser);
        if (i != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public void deleteFood(Integer id) {
        Permission.check(UserType.USER.getCode());
        User user = permissionVerify(PermissionEnum.DELETE_PERMISSION.getCode());
        FoodUser foodUser = foodUserMapper.selectById(id);
        if (foodUser == null || !foodUser.getUserId().equals(user.getId())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        int i = foodUserMapper.deleteById(id);
        if (i != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }


    @Override
    public IPage<FoodUser> getFoodList(SearchFood searchFood,Integer code) {
        Permission.check(UserType.USER.getCode());
        String token = ThreadLocalUtil.get();
        if (token == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        Integer userId = JWTHelper.getUserId(token);
        if (userId == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        //根据用户id并且过期时间大于当前时间
        LambdaQueryWrapper<FoodUser> foodCompanyLambdaQueryWrapper = new LambdaQueryWrapper<FoodUser>().eq(FoodUser::getUserId, userId);
        if (code.equals(ExpiredStatusEnum.NOT_EXPIRED.getCode())){
            foodCompanyLambdaQueryWrapper.gt(FoodUser::getExpirationTime, LocalDate.now());
        }else {
            foodCompanyLambdaQueryWrapper.lt(FoodUser::getExpirationTime, LocalDate.now());
        }

        Page<FoodUser> page = new Page<>(searchFood.getPageNum(), searchFood.getPageSize());
        return foodUserMapper.selectPage(page, foodCompanyLambdaQueryWrapper);

    }


    private User permissionVerify(String code) {
        String token = ThreadLocalUtil.get();
        if (token == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        Integer userId = JWTHelper.getUserId(token);
        if (userId == null || userId <= 0){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        User user = userMapper.selectById(userId);
        if (user == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (code.equals(PermissionEnum.ADD_PERMISSION.getCode())){
            if (!user.getAddPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
            }
        } else if (code.equals(PermissionEnum.UPDATE_PERMISSION.getCode())) {
            if (!user.getUpdataPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Updated);
            }
        } else if (code.equals(PermissionEnum.DELETE_PERMISSION.getCode())) {
            if (!user.getDeletePermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Deleted);
            }
        } else if (code.equals(PermissionEnum.SUBMIT_EXPIRED_ITEM_PERMISSION.getCode())) {
            if (!user.getSubmitExpiredItemPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
            }
        }else {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        return user;
    }
}
