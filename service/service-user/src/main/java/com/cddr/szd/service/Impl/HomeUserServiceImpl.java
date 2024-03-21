package com.cddr.szd.service.Impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;

import com.cddr.szd.login.ThreadLocalUtil;
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

@Service
public class HomeUserServiceImpl implements HomeUserService {
    @Autowired
    private HomeUserFoodMapper homeUserFoodMapper;
    @Autowired
    private UserMapper userMapper;

    Permission permission = new Permission();


    @Override
    public void addFood(FoodUserVo foodUserVo){


        //从本地线程中获取token
        DecodedJWT o = ThreadLocalUtil.get();
        //从token中获取到id和type
        int userId = o.getClaim("user_id").asInt();
        Integer type = o.getClaim("type").asInt();
        if (type.equals(UserType.USER.getCode())){
            throw new BizException(BizCodeEnum.Wrong_Role);
        }
        //有没有权限
        if(check(userId,1)==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }
        FoodUser foodUser = new FoodUser();

        BeanUtils.copyProperties(foodUserVo, foodUser);
        foodUser.setUserId(userId);

        int insert = homeUserFoodMapper.insert(foodUser);
        if (insert == 0){
            throw new BizException(BizCodeEnum.Failed_To_Add);
        }
    }

    @Override
    public void updateFood(FoodUserVo foodUserVo) {
        //从本地线程中获取token
        DecodedJWT o = ThreadLocalUtil.get();
        //从token中获取到id和type
        int userId = o.getClaim("user_id").asInt();
        Integer type = o.getClaim("type").asInt();
        if (type.equals(UserType.USER.getCode())){
            throw new BizException(BizCodeEnum.Wrong_Role);
        }
        //有没有权限
        if(check(2)==0){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
        }

        LambdaUpdateWrapper<FoodUser> lambdaUpdateWrapper = Wrappers.<FoodUser>lambdaUpdate()
                .eq(FoodUser::getId, foodUserVo.getId()) // 设置更新条件为 id 等于给定值
                .set(FoodUser::getFoodCreatetime, foodUserVo.getFoodCreatetime())
                .set(FoodUser::getFoodExpirationTime, foodUserVo.getFoodExpirationTime())
                .set(FoodUser::getNameFood, foodUserVo.getNameFood());
//        homeUserFoodMapper.updateById()

        int result = homeUserFoodMapper.update(null, lambdaUpdateWrapper); // 执行更新操作
        if(result<=0){
            throw new BizException(BizCodeEnum.Failed_To_Update);
        }

    }

    @Override
    public IPage<FoodUser> getAllFood(FoodUserSearchVo foodUserSearchVo){
        if(!permission.check(2)){
            throw new BizException(BizCodeEnum.Wrong_Role);
        }





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
                else {
                    throw new BizException(BizCodeEnum.Wrong_Role);
                }
            }
            else {
                throw new BizException(BizCodeEnum.Wrong_Role);
            }


    }
}
