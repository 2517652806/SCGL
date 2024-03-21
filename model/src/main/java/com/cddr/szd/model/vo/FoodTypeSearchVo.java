package com.cddr.szd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTypeSearchVo {
    private String typeName;
    @NotNull
    private Integer pageNum = 1;
    @NotNull
    private Integer pageSize = 10;
}
