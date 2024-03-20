package com.cddr.szd.controller;

import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.FoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food/type")
public class FoodTypeController {
    @Autowired
    FoodTypeService foodTypeService;
    @PostMapping("/add")
    public Result addFood(@RequestBody FoodType foodType){
        foodTypeService.add(foodType);
        return Result.ok(null);
    }
    //获取所有食品类型
    @GetMapping("/getAllFoodType")
    public Result getAllFoodType(FoodTypeSearchVo foodTypeSearchVo){
        return Result.ok(foodTypeService.getAllFoodType(foodTypeSearchVo));
    }
}
