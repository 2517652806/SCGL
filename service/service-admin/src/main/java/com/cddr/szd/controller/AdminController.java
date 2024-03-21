package com.cddr.szd.controller;

import com.cddr.szd.model.RegularUser;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    /**
     * 添加企业用户
     * @return
     */
    @PostMapping("add/user")
    public Result addUser(@RequestBody @Validated RegularUser regularUser){
        adminService.addUser(regularUser);
        return Result.ok("操作成功");
    }
}
