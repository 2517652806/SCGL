package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.FoodUser;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.model.vo.FoodUserSearchVo;
import com.cddr.szd.model.vo.FoodUserVo;

public interface HomeUserService {

    void addFood(FoodUserVo foodUserVo);
    void updateFood(FoodUserVo foodUserVo);
//    void addFood(FoodUserVo foodVo);
    IPage<FoodUser> getAllFood(FoodUserSearchVo foodUserSearchVo);


}
