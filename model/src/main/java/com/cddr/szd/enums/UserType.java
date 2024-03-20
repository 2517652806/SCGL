package com.cddr.szd.enums;

import com.cddr.szd.model.User;
import lombok.Getter;

@Getter
public enum UserType {
    USER(1, "用户"),
    EMPLOYEE(2, "员工"),
    ADMIN(3, "管理员");

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
