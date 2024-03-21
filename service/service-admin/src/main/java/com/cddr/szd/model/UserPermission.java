package com.cddr.szd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {
    @NotNull
    private Integer id;
    private Integer addPermission = 0;
    private Integer updataPermission = 0;
    private Integer deletePermission = 0;
    private Integer submitExpiredItemPermission = 0;
}
