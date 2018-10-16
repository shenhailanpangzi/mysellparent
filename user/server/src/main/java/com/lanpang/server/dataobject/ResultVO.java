package com.lanpang.server.dataobject;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;
    private String msg;
    private T data;

}
