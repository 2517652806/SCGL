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
    @NotNull(groups = Update.class)
    private Integer id;
    @NotNull
    private String name;//食品名称
    @NotNull
    private LocalDate procurementTime;//采购日期
    @NotNull
    private LocalDate expirationTime;//过期时间

    public interface Update extends Default {

    }

}
