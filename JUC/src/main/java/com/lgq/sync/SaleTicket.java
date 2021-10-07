package com.lgq.sync;
class Ticket {
    //  票数
    private volatile int num = 30;
    //  卖票方法,不加synchronized可能会出现重复卖同一张票
    public synchronized void sale() {
        if (num > 0) {
            System.out.println(Thread.currentThread().getName() + " 卖出：" + (num--) + " 剩下：" + num);
        }
    }
}
public class SaleTicket {
    //    创建多个线程卖票
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 30; j++) {
                    ticket.sale();
                }
            }, "sale-" + i).start();
        }
    }
}
