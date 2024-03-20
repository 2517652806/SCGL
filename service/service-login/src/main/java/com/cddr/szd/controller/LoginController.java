package com.cddr.szd.controller;

import com.cddr.szd.loginModel.RegularUser;
import com.cddr.szd.model.vo.UserVo;
import com.cddr.szd.result.Result;
import com.cddr.szd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     */
    @GetMapping("/getCaptcha")
    public Result getCaptcha(@RequestParam() @Email(message = "请输入合法的邮箱") String emailNum) {
        userService.getCaptcha(emailNum);
        return Result.ok(null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegularUser registerUser) {
        userService.register(registerUser);
        return Result.ok(null);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserVo userVo){
        String token = userService.login(userVo);
        return Result.ok(token);
    }
    /**
     * 修改密码
     */
    @PostMapping("/updatePwd")
    public Result updatePassword(@RequestBody Map<String, String> params) {
        //1.校验参数
        String captcha = params.get("captcha");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");
        if (!StringUtils.hasLength(captcha) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.fail("缺少必要参数");
        }
        userService.updatePassword(captcha,newPwd,rePwd);
        return Result.ok("操作成功");
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.ok("操作成功");
    }
}
