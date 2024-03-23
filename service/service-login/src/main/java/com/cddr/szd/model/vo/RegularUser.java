package com.cddr.szd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * 普通用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegularUser {
    @NotNull(groups = Update.class)
    private Integer id;
    @NotNull
    private String name; //账号
    @NotNull(groups = Add.class)
    private String password;//密码
    @NotNull(groups = Add.class)
    private String confirmPassword;//确认密码

    @NotNull
    @Email
    private String email;//邮箱

    @NotNull
    private String captcha;//验证码

    public interface Update extends Default {

    }

    public interface Add extends Default {

    }
}
