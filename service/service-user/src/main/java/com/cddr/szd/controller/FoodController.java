package com.cddr.szd.controller;

import com.cddr.szd.enums.ExpiredStatusEnum;
import com.cddr.szd.model.FoodVo;
import com.cddr.szd.model.SearchFood;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
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
        return Result.ok(foodService.getFoodList(searchFood, ExpiredStatusEnum.NOT_EXPIRED.getCode()));
    }

    /**
     * 获取食材列表
     */
    @GetMapping("/getExpiredFoodList")
    public Result getExpiredFoodList(SearchFood searchFood) {
        if (searchFood.getPageNum() == null){
            searchFood.setPageNum(1);
        }
        if (searchFood.getPageSize() == null){
            searchFood.setPageSize(10);
        }
        return Result.ok(foodService.getFoodList(searchFood, ExpiredStatusEnum.EXPIRED.getCode()));
    }

}
