package com.cddr.szd.model;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class FoodType implements Serializable {

    @TableId(type = IdType.AUTO)
    @NotNull
    private Integer id;
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String foodType;

    @NotNull
    private String companyName; //公司名称

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


}
