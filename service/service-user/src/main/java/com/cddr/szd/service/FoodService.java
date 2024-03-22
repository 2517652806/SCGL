package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cddr.szd.model.*;

import java.util.List;

public interface FoodService {
    void purchaseFood(FoodVo foodVo);


    void updateFood(FoodVo foodVo);

    void deleteFood(Integer id);


    IPage<FoodUser> getFoodList(SearchFood searchFood,Integer code);
}
