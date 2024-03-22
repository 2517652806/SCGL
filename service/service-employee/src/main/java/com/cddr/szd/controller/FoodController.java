package com.cddr.szd.controller;

import com.cddr.szd.model.FoodVo;
import com.cddr.szd.model.SearchFood;
import com.cddr.szd.service.FoodService;
import com.cddr.szd.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class FoodController {
    @Autowired
    private FoodService foodService;

    /**
     * 食材采购
     */
    @PostMapping("/purchase")
    public Result purchaseFood(@RequestBody @Validated FoodVo foodVo) {
        foodService.purchaseFood(foodVo);
        return Result.ok(null);
    }
    /**
     * 修改食材
     */
    @PutMapping("/update")
    public Result updateFood(@RequestBody @Validated(FoodVo.Update.class) FoodVo foodVo) {
        foodService.updateFood(foodVo);
        return Result.ok(null);
    }

    /**
     * 删除食材
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        foodService.deleteFood(id);
        return Result.ok(null);
    }

    /**
     * 获取食材分类
     * @return
     */
    @GetMapping("/allFoodType")
    public Result getAllFoodType(){
        return Result.ok(foodService.getAllFoodType());
    }

    /**
     * 将过期食材提交到待处理列表
     */
    @PutMapping("/expired/{id}")
    public Result expiredFood(@PathVariable Integer id) {
        foodService.expiredFood(id);
        return Result.ok(null);
    }

    /**
     * 获取食材列表
     */
    @GetMapping("/getFoodList")
    public Result getFoodList(SearchFood searchFood) {
        if (searchFood.getPageNum() == null){
            searchFood.setPageNum(1);
        }
        if (searchFood.getPageSize() == null){
            searchFood.setPageSize(10);
        }
        return Result.ok(foodService.getFoodList(searchFood));
    }

    /**
     * 获取过期食材列表
     */
    @GetMapping("/getExpiredFoodList")
    public Result getExpiredFoodList(SearchFood searchFood) {
        if (searchFood.getPageNum() == null){
            searchFood.setPageNum(1);
        }
        if (searchFood.getPageSize() == null){
            searchFood.setPageSize(10);
        }
        return Result.ok(foodService.getExpiredFoodList(searchFood));
    }

    /**
     * 数据分析
     */
    @GetMapping("/analysis/{type}")
    public Result analysis(@PathVariable Integer type){
        return Result.ok(foodService.getAnalysis(type));
    }
}
