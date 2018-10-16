package com.lanpang.server.exception;


import com.lanpang.server.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by 杨浩
 * 2018-06-11 18:55
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
