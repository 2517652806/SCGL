package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cddr.szd.model.AnalysisData;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.vo.FoodTypeSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FoodTypeService {
    //添加的时候，要check如果没有就添加到这个表里，如果有就直接写
    void add (FoodType foodType);

    //分页查询
    IPage<FoodType> getFoodType(FoodTypeSearchVo foodTypeSearchVo);

    void updateFoodType(FoodType foodType);

    void deleteFoodType(Integer id);


    ArrayList<AnalysisData> getAnalysis(Integer type);
}
