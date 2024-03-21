package com.cddr.szd.service.Impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        return homeUserFoodMapper.selectPage(page, null);
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
