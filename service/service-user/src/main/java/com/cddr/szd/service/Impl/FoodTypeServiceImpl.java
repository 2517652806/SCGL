package com.cddr.szd.service.Impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;

import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.mapper.FoodTypeMapper;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.result.ResultCodeEnum;
import com.cddr.szd.service.FoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodTypeServiceImpl implements FoodTypeService {
    @Autowired
    FoodTypeMapper foodTypeMapper;
    @Override
    public void add(FoodType foodType){
        Permission.check(UserType.ADMIN.getCode());
        int insert = foodTypeMapper.insert(foodType);
        if (insert == 0){
            throw new BizException(BizCodeEnum.Failed_To_Add);
        }

    }


    @Override
    public boolean check(FoodType foodType){
        LambdaQueryWrapper<FoodType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FoodType::getFoodType, foodType.getFoodType()); // 查询foodName等于指定值的记录
        return foodTypeMapper.selectCount(lambdaQueryWrapper) > 0; // 如果记录数大于0，则表示存在
    }




    @Override
    public IPage<FoodType> getAllFoodType(FoodTypeSearchVo foodTypeSearchVo){
        Page<FoodType> page = new Page<>(foodTypeSearchVo.getPageNum(), foodTypeSearchVo.getPageSize());
        return foodTypeMapper.selectPage(page, null);
    }
    //通过jwt传的useid，查有没有权限操作，管理员type可以操作
    @Override
    public void updateFoodType(FoodType foodType){
        Permission.check(UserType.ADMIN.getCode());
        LambdaUpdateWrapper<FoodType> lambdaUpdateWrapper = Wrappers.<FoodType>lambdaUpdate()
                .eq(FoodType::getId, foodType.getId()) // 设置更新条件为 id 等于给定值
                .set(FoodType::getFoodType, foodType.getFoodType()); // 设置 name 字段的新值

        int result = foodTypeMapper.update(null, lambdaUpdateWrapper); // 执行更新操作
        if(result<=0){
            throw new BizException(BizCodeEnum.Failed_To_Update);
        }

    }
    @Override
    public void deleteFoodType(Integer id){

    }
}
