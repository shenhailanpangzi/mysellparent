package com.lanpang.server.utils;


import com.lanpang.server.VO.ResultVO;

/**
 * Created by 杨浩
 * 2018-05-15 00:22
 */
public class ResultVOUtil {

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
