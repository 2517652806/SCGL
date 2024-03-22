package com.cddr.szd.enums;

import lombok.Getter;

@Getter
public enum  ExpiredStatusEnum {
    NOT_EXPIRED(0, "未过期"),
    EXPIRED(1, "已过期");
    private Integer code;
    private String msg;
    private ExpiredStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static String getMsg(int code) {
        for (ExpiredStatusEnum expiredStatusEnum : ExpiredStatusEnum.values()) {
            if (expiredStatusEnum.getCode() == code) {
                return expiredStatusEnum.getMsg();
            }
        }
        return null;
    }
    public static Integer getCode(String msg) {
        for (ExpiredStatusEnum expiredStatusEnum : ExpiredStatusEnum.values()) {
            if (expiredStatusEnum.getMsg().equals(msg)) {
                return expiredStatusEnum.getCode();
            }
        }
        return null;
    }
}
