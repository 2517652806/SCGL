package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUser {
    private String name;
    private String email;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
