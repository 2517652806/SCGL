package com.cddr.szd.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;

import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.mapper.FoodTypeMapper;
import com.cddr.szd.mapper.HomeUserFoodMapper;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.FoodUser;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.User;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.model.vo.FoodUserSearchVo;
import com.cddr.szd.model.vo.FoodUserVo;
import com.cddr.szd.service.HomeUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HomeUserServiceImpl implements HomeUserService {
    @Autowired
    private HomeUserFoodMapper homeUserFoodMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FoodTypeMapper foodTypeMapper;


    @Override
    public void addFood(FoodUserVo foodUserVo){

        Permission.check(UserType.USER.getCode());

        if(check(UserType.ADD_PERMISSION.getCode())==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }
        FoodUser foodUser = new FoodUser();
        DecodedJWT o = ThreadLocalUtil.get();
        Integer id = o.getClaim("id").asInt();
        BeanUtils.copyProperties(foodUserVo, foodUser);
        foodUser.setUserId(id);//设置userid谁采购的

        int insert = homeUserFoodMapper.insert(foodUser);
        if (insert == 0){
            throw new BizException(BizCodeEnum.Failed_To_Add);
        }
    }

    @Override
    public void updateFood(FoodUserVo foodUserVo) {
        Permission.check(UserType.USER.getCode());
        //有没有权限
        if(check(UserType.UPDATE_PERMISSION.getCode())==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }

        LambdaUpdateWrapper<FoodUser> lambdaUpdateWrapper = Wrappers.<FoodUser>lambdaUpdate()
                .eq(FoodUser::getId, foodUserVo.getId()) // 设置更新条件为 id 等于给定值
                .set(FoodUser::getFoodCreatetime, foodUserVo.getFoodCreatetime())
                .set(FoodUser::getFoodExpirationTime, foodUserVo.getFoodExpirationTime())
                .set(FoodUser::getNameFood, foodUserVo.getNameFood());

        int result = homeUserFoodMapper.update(null, lambdaUpdateWrapper); // 执行更新操作
        if(result<=0){
            throw new BizException(BizCodeEnum.Failed_To_Update);
        }

    }
    //分页获取该用户采购食材数据
    @Override
    public IPage<FoodUser> getAllFood(FoodUserSearchVo foodUserSearchVo){
        Permission.check(UserType.USER.getCode());
        Page<FoodUser> page = new Page<>(foodUserSearchVo.getPageNum(), foodUserSearchVo.getPageSize());
        //在分页查询之前，把所有现在已经过期的食材的状态改为2，过期的食材状态为2
        UpdateWrapper<FoodUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("state", 2) // 设置要更新的字段及其值
                .lt("food_state", new Date()); // 添加条件：expiryDate小于当前日期
        // 执行更新操作
        int updatedCount = homeUserFoodMapper.update(null, updateWrapper); // 第一个参数为null，因为实体对象在这里不需要

        DecodedJWT o = ThreadLocalUtil.get();
        int userId = o.getClaim("id").asInt();
        LambdaQueryWrapper<FoodUser> queryWrapper = Wrappers.<FoodUser>lambdaQuery();
        queryWrapper.eq(FoodUser::getUserId, userId)
                .eq(FoodUser::getFoodExpiredImpression, 0);//过滤过期展示区的数据


        return homeUserFoodMapper.selectPage(page, queryWrapper);
    }
    //删除食材信息
    @Override
    public void deleteFoodType(Integer id) {
        Permission.check(UserType.USER.getCode());
        if (check(UserType.DELETE_PERMISSION.getCode())==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }
        if (homeUserFoodMapper.deleteById(id)<=0){
            throw new BizException(BizCodeEnum.Failed_To_Delete);
        }

    }
    //设置过期状态
    @Override
    public void updateExpiredImpressionFood(FoodUser foodUser) {
        Permission.check(UserType.USER.getCode());
        if (check(UserType.SUBMIT_EXPIRED_ITEM_PERMISSION.getCode())==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }
        foodUser.setFoodState(1);//设置为过期状态
        if(homeUserFoodMapper.updateById(foodUser)<=0){
            throw new BizException(BizCodeEnum.Failed_To_Update);
        }
    }

    @Override
    public List<FoodType> allFootType() {
        Permission.check(UserType.USER.getCode());
        LambdaQueryWrapper<FoodType> eq = new LambdaQueryWrapper<FoodType>().eq(FoodType::getCompanyName, "home");
        return foodTypeMapper.selectList(eq);
    }


    //判断用户是否有操作权限
    public Integer check(Integer a){//
        DecodedJWT o = ThreadLocalUtil.get();
        int userId = o.getClaim("id").asInt();
        QueryWrapper<User> queryWrapper = Wrappers.<User>query().eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
            if (user != null) {
                if (a == 1){
                    return user.getAddPermission();
                }
                else if (a == 2){
                    return user.getUpdataPermission();
                }
                else if (a == 3){
                    return user.getDeletePermission();//返回删除权限
                }
                else if (a == 4){
                    return user.getSubmitExpiredItemPermission();
                }
                else {
                    throw new BizException(BizCodeEnum.Wrong_Role);
                }
            }
            else {
                throw new BizException(BizCodeEnum.Wrong_Role);
            }


    }
}
