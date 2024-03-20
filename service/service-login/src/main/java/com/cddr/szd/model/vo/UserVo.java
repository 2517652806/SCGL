package com.cddr.szd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    @NotNull(message = "用户名不能为空")
    private String userName;//用户名
    @NotNull(message = "密码不能为空")
    private String password;//密码
}
