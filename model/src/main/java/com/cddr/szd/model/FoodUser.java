package com.cddr.szd.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
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
public class FoodUser implements Serializable {


    @TableId(type = IdType.AUTO)
    @NotNull
    private Integer id;
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String name;


    /**
     * 食物采购时间
     */

    private LocalDate procurementTime;

    /**
     * 食物过期时间
     */
    private LocalDate expirationTime;


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
     * 用户Id
     */
    private Integer userId;


}
