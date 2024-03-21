package com.cddr.szd.enums;

import lombok.Getter;

@Getter
public enum UserType {
    USER(2, "家庭用户"),
    EMPLOYEE(1, "员工"),
    ADMIN(0, "管理员"),
    ADD_PERMISSION(1,"添加"),
    DELETE_PERMISSION(3,"删除"),
    UPDATE_PERMISSION(2,"修改"),
    SUBMIT_EXPIRED_ITEM_PERMISSION(4,"过期提交"),
    ;
    public static UserType getByCode(Integer code) {
        UserType[] values = UserType.values();
        for (UserType userType : values) {
            if (userType.getCode().equals(code)) {
                return userType;
            }
        }
        return null;
    }
    public static UserType getByName(String name){
        UserType[] values = UserType.values();
        for (UserType userType : values) {
            if (userType.getName().equals(name)){
                return userType;
            }
        }
        return null;
    }

    private Integer code;
    private String name;
    private UserType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
