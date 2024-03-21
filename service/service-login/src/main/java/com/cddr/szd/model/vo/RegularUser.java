package com.cddr.szd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * 普通用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegularUser {
    @NotNull
    private String name; //账号
    @NotNull
    private String password;//密码
    @NotNull
    private String confirmPassword;//确认密码

    @NotNull
    @Email
    private String email;//邮箱

    @NotNull
    private String captcha;//验证码
}
