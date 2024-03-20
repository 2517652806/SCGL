package com.cddr.szd.controller;

import com.cddr.szd.model.vo.FoodVo;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.HomeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class HomeUserController {

    @Autowired
    private HomeUserService homeUserService;
    @PostMapping("/addFood")
    public Result addFood(@RequestBody  @Validated(FoodVo.Add.class) FoodVo foodVo){
        homeUserService.addFood(foodVo);
        return Result.ok(null);
    }


}
