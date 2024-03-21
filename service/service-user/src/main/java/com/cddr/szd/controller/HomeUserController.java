package com.cddr.szd.controller;

import com.cddr.szd.model.FoodUser;
import com.cddr.szd.model.vo.FoodUserSearchVo;
import com.cddr.szd.model.vo.FoodUserVo;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.HomeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class HomeUserController {

    @Autowired
    private HomeUserService homeUserService;
    @PostMapping("/addFood")
    public Result addFood(@RequestBody  @Validated(FoodUserVo.Add.class) FoodUserVo foodUserVo){
        homeUserService.addFood(foodUserVo);
        return Result.ok(null);
    }
    @PostMapping("/update")
    public Result updateFood(@RequestBody  @Validated(FoodUserVo.Update.class) FoodUserVo foodUserVo){
        homeUserService.updateFood(foodUserVo);
        return Result.ok(null);
    }
    //获取所有食材
    @GetMapping ("/findAll")
    public Result findFood(FoodUserSearchVo foodUserSearchVo){
        homeUserService.getAllFood(foodUserSearchVo);
        return Result.ok(null);
    }
    //通过id删除记录
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        homeUserService.deleteFoodType(id);
        return Result.ok(null);
    }
    //把食材添加到过期展示
    @PostMapping("/updateExpiredImpression")
    public Result updateExpiredImpressionFood(@RequestBody FoodUser foodUser){
        homeUserService.updateExpiredImpressionFood(foodUser);
        return Result.ok(null);
    }
    //除开的过期区的数据的模糊查询
    @GetMapping("/fuzzyFindFood")
    public Result fuzzyFindFood(@RequestBody  @Validated(FoodUserVo.Update.class) FoodUserVo foodUserVo){
        homeUserService.fuzzyFindFood(foodUserVo);
        return Result.ok(null);
    }

}
