package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFood {
    private String foodName;
    private String sourceName;
    private Integer foodTypeId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
