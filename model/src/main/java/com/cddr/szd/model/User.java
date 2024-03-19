package com.cddr.szd.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    @TableId(type = IdType.AUTO)
    @NotNull
    private Integer id;//主键ID

    private String userName;//用户名
    //springMvc把当前对象转换成json对象的时候，忽略password最终的json字符串就没有password这个属性了
    @JsonIgnore
    private String password;//密码

//    @NotEmpty
//    @Pattern(regexp = "^\\S{1,10}$")
//    private String nickname;//昵称

    @NotEmpty
    @Email
    private String email;//邮箱


    private String userPic;//用户头像地址

    private Integer type;//用户类型 0管理员 1普通用户
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;//更新时间

    @TableLogic
    private Integer isDeleted; //逻辑删除
}
