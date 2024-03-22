package com.cddr.szd.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FoodCompany implements Serializable {
    @TableId(type = IdType.AUTO)
    @NotNull
    private Integer id;
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 食物采购时间
     */

    private LocalDate procurementTime;

    /**
     * 食物过期时间
     */
    private LocalDate expirationTime;

    /**
     * 食物重量单位kg
     */
    private Double weight;

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
     * 采购源是哪家公司
     */
    private String source;

    /**
     * 食物种类
     */
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private Integer typeId;

    /**
     * 1->没有过期 2->过期
     */
    private Integer state;
    /**
     * 0->没有进入过期展示区 1->进入了过期展示区
     */
    private Integer expiredImpression;
    /**
     * 公司名
     */
    private String companyName;
}
