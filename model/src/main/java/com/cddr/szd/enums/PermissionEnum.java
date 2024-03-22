package com.cddr.szd.enums;

import lombok.Getter;

@Getter
public enum PermissionEnum {
    DELETE_PERMISSION("delete", "食材删除"),
    UPDATE_PERMISSION("update", "食材修改"),
    ADD_PERMISSION("add", "食材添加"),
    SUBMIT_EXPIRED_ITEM_PERMISSION("submit", "食材过期提交");
    private String code;
    private String message;
    private PermissionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public static PermissionEnum getByCode(String code) {
        for (PermissionEnum permissionEnum : PermissionEnum.values()) {
            if (permissionEnum.getCode().equals(code)) {
                return permissionEnum;
            }
        }
        return null;
    }
    public static PermissionEnum getByMessage(String message) {
        for (PermissionEnum permissionEnum : PermissionEnum.values()) {
            if (permissionEnum.getMessage().equals(message)) {
                return permissionEnum;
            }
        }
        return null;
    }

}
