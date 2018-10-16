package com.lanpang.server.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    BUYER(1L, "买家"),
    SELLER(2L, "卖家");

    RoleEnum(Long code, String des) {
        this.code = code;
        this.des = des;
    }

    private Long code;

    private String des;

}
