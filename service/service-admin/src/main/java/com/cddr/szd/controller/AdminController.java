package com.cddr.szd.controller;

import com.cddr.szd.model.RegularUser;
import com.cddr.szd.model.SearchUser;
import com.cddr.szd.model.UserPermission;
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
        return Result.ok(null);
    }

    /**
     * 修改用户信息
     */
    @PutMapping("update/user")
    public Result updateUser(@RequestBody @Validated(RegularUser.Update.class) RegularUser regularUser){
        adminService.updateUser(regularUser);
        return Result.ok(null);
    }

    /**
     * 删除用户信息
     */
    @DeleteMapping("delete/user/{id}")
    public Result deleteUser(@PathVariable Integer id ){
        adminService.deleteUser(id);
        return Result.ok(null);
    }

    /**
     * 分页查询用户信息
     */
    @GetMapping("find/user")
    public Result findUser(SearchUser searchUser){
        return Result.ok(adminService.findUser(searchUser));
    }

    /**
     * 修改用户权限
     */
    @PutMapping("update/user/permission")
    public Result updateUserPermission(@RequestBody UserPermission userPermission){
        adminService.updatePermission(userPermission);
        return Result.ok(null);
    }

    /**
     * 重置密码
     */
    @PutMapping("reset/password/{id}")
    public Result resetPassword(@PathVariable Integer id){
        adminService.resetPassword(id);
        return Result.ok(null);
    }
}
