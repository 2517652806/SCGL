package com.cddr.szd.model;

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
    private String companyName;//企业名

    @NotNull
    private String name; //账号

    @NotNull
    @Email
    private String email;//邮箱

    @NotNull
    private String captcha;//验证码
}
