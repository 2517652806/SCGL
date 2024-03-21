package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUser {
    private String name;
    private String email;
    @NotNull
    private Integer pageNum = 1;
    @NotNull
    private Integer pageSize = 10;
}
