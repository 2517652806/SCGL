package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodVo implements Serializable {
    @NotNull(groups = FoodVo.Update.class)
    private Integer id;
    @NotNull
    private String source; //采购来源
    @NotNull
    private String name;//食品名称
    @NotNull
    private Integer typeId;//食品分类
    @NotNull
    private LocalDate procurementTime;//采购日期
    @NotNull
    private LocalDate expirationTime;//过期时间
    @NotNull
    private BigDecimal price;//价格
    @NotNull
    private Double weight; //重量

    public interface Update extends Default {

    }

}
