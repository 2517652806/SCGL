package com.cddr.szd.controller;

import com.cddr.szd.result.Result;
import com.cddr.szd.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("")
    public Result login(String username, String password){
        return Result.ok("123");
    }

    /**
     * 发送验证码
     */
    @GetMapping("/getCaptcha")
    public Result getCaptcha(@RequestParam() @Email(message = "请输入合法的邮箱") String emailNum) {
        loginService.getCaptcha(emailNum);
        return Result.ok(null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterUser registerUser) {
        loginService.register(registerUser);
        return Result.ok(null);
    }

}
