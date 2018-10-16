package com.lanpang.server.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    LOGIN_FAIL(1, "未查询到该用户"),
    ERROR_ROLE(2, "该用户权限不正确")
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
