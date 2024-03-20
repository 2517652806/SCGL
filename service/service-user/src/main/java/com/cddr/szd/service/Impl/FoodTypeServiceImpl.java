package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.exception.BizException;

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

//        DecodedJWT o = ThreadLocalUtil.get();
//        Integer type = o.getClaim("type").asInt();
//        if (type.equals(UserType.ADMIN.getCode())){
//            throw new BizException(BizCodeEnum.ADMIN_NOT_CASES);
//        }


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
    @Override
    public void updateFoodType(FoodType foodType){

    }

    public void deleteFoodType(Integer id){

    }
}
