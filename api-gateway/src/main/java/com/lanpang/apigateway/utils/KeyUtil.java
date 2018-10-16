package com.lanpang.apigateway.utils;

import java.util.Random;

/**
 * Created by 杨浩
 * 2018-06-11 19:12
 */
public class KeyUtil {

    /**
     * 生成唯一的主键 并不能100%唯一
     * 格式: 时间+随机数
     *
     * @return
     */
    //synchronized关键字 在执行synchronized代码块时会锁定当前的对象，
    // 只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        //生成6位随机数 随机生成一个数，然后加上6位
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
