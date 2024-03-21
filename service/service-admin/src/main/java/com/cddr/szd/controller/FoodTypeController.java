package com.cddr.szd.controller;

import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.FoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class FoodTypeController {
    @Autowired
    FoodTypeService foodTypeService;
    @PostMapping("/add/foodType")
    public Result addFood(@RequestBody FoodType foodType){
        foodTypeService.add(foodType);
        return Result.ok(null);
    }
    //获取所有食品类型
    @GetMapping("/getFoodType")
    public Result getFoodType(@Validated FoodTypeSearchVo foodTypeSearchVo){
        return Result.ok(foodTypeService.getFoodType(foodTypeSearchVo));
    }
    @PutMapping("/update/foodType")
    public Result updateFoodType(@RequestBody FoodType foodType){
        foodTypeService.updateFoodType(foodType);
        return Result.ok(null);
    }
    @DeleteMapping("/delete/foodType/{id}")
    public Result delete(@PathVariable Integer id){
        foodTypeService.deleteFoodType(id);
        return Result.ok(null);
    }
    @GetMapping("/getAllFoodType")
    public Result getAllFoodType(){
        return Result.ok(foodTypeService.getAllFoodType());
    }
}
