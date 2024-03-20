package com.cddr.szd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTypeSearchVo {
    private Integer type;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
