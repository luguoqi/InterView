package com.lgq.distribute.lock;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 12:24
 * @Version 1.0
 */
public class OrderNumCreateUtil {
    private static int number = 0;//资源

    public String getOrderNumber() {
        return "\t 生成订单编号：" + (++number);
    }
}
