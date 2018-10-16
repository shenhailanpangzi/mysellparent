package com.lanpang.server.exception;


import com.lanpang.server.enums.ResultEnum;
import lombok.Getter;

/**
 * 例如调用银行接口调用失败时，httpStatus不要返回200
 * Created by 杨浩
 * 2018-06-11 18:55
 */
@Getter
public class ResponseBankException extends RuntimeException {
    private Integer code;

    public ResponseBankException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public ResponseBankException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
