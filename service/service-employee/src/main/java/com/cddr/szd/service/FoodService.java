package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cddr.szd.model.*;

import java.util.ArrayList;
import java.util.List;

public interface FoodService {
    void purchaseFood(FoodVo foodVo);

    List<FoodType> getAllFoodType();

    void updateFood(FoodVo foodVo);

    void deleteFood(Integer id);

    void expiredFood(Integer id);

    IPage<FoodCompany> getFoodList(SearchFood searchFood);

    IPage<FoodCompany> getExpiredFoodList(SearchFood searchFood);

    ArrayList<AnalysisData> getAnalysis(Integer type);
}
