package com.lgq.distribute.lock;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 12:26
 * @Version 1.0
 */
public class Client {
    public static void main(String[] args) {
//        OrderService orderService = new OrderService();
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                public void run() {
                    OrderService orderService = new OrderService();
                    String orderNumber = orderService.getOrderNumber();
                    System.out.println(orderNumber);
                }
            }, String.valueOf(i)).start();

        }
    }
}
