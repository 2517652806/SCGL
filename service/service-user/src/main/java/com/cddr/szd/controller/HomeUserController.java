package com.cddr.szd.controller;

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
    @GetMapping ("/findAll")
    public Result findFood(){
        homeUserService.
        return Result.ok(null);
    }


}
