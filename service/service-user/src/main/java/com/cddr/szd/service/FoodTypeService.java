package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.vo.FoodTypeSearchVo;

public interface FoodTypeService {
    //添加的时候，要check如果没有就添加到这个表里，如果有就直接写
    void add (FoodType foodType);

    boolean check (FoodType foodType);

    //分页查询
    IPage<FoodType> getAllFoodType(FoodTypeSearchVo foodTypeSearchVo);

    void updateFoodType(FoodType foodType);

    void deleteFoodType(Integer id);
}
