package com.cddr.szd.controller;

import com.cddr.szd.helper.JWTHelper;
import com.cddr.szd.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class LoginController {
    @GetMapping("/login")
    public String test(){
        System.out.println("lllll");
        User user = new User();
        user.setId(1);
//        String token = JWTHelper.createToken(user);
//        return token;
        return "1313";
    }

}
