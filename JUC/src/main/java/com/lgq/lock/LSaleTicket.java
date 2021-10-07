package com.lgq.lock;

import java.util.concurrent.locks.ReentrantLock;

class LTicket {
    //  票数
    private volatile int num = 30;
    private final ReentrantLock lock = new ReentrantLock();
    //  卖票方法,不加synchronized可能会出现重复卖同一张票
    public void sale() {
//        上锁
        lock.lock();
        try {
            if (num > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出：" + (num--) + " 剩下：" + num);
            }
        } finally {
//        解锁
        lock.unlock();
        }
    }
}
public class LSaleTicket {
    //    创建多个线程卖票
    public static void main(String[] args) {
        LTicket ticket = new LTicket();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 30; j++) {
                    ticket.sale();
                }
            }, "sale-" + i).start();
        }
    }

}
