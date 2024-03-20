package com.cddr.szd.model;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 
 * </p>
 *
 * @author zy
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    @NotNull
    private Integer id;
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String name;
    @NotEmpty
    @Email
    private String email;

    @JsonIgnore
    private String password;

    /**
     * 采购权限
     */
    private Integer addPermission;

    /**
     * 修改权限
     */
    private Integer updataPermission;

    /**
     * 删除权限
     */
    private Integer deletePermission;

    /**
     * 将过期食物提交为过期状态的权限
     */
    private Integer submitExpiredItemPermission;

    /**
     * 0->管理员，1—》企业用户，2—》用户
     */
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 公司名
     */
    private String companyName;


}
