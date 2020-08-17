package com.yango;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Data {
//    public  int num = 0;
    public volatile int num = 0;
    public void changeData() {
        num = 99;
    }

    public synchronized void add1() {
        int x = 1;
        int y =2;
        x = x + 5;
        y = x * x;

        num++;
    }
    public AtomicInteger atomicInteger = new AtomicInteger();
    public void add() {
        atomicInteger.getAndIncrement();
    }

}

class VolatileTest {
    /**
     * volatile 原子性测试(不保证原子性)
     * @param args
     */
    public static void main(String[] args) {
        Data data = new Data();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    data.add1();
                    data.add();
                }
            }, "thread" + i ).start();
        }
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println("===================");
        System.out.println(data.num);
        System.out.println(data.atomicInteger);
    }
    /**
     * volatile 可见性测试(本例使用两个线程，即main和A线程)
     *  情况1.变量num未添加  volatile 启动main线程卡死
     *  情况2.变量num添加 volatile 启动main线程，可正常执行结束
     *  结论：未添加volatile，A线程执行 3s后 先将变量num的值拷贝至线程的工作内存，然后修改其值为99，之后再将其值写回主物理内存，而在A线程拷贝num值之前，main线程已经将num值拷贝至其物理内存中，
     *      所以当A线程修改过num值之后，main线程并未重新将拷贝num值，从而导致main线程中的num值永远为0，所以出现死循环。
     *      添加volatile，由于volatile能保证可见性，当A线程改变了num值之后会通知main线程，此时main线程会重新从主内存中拷贝最新值至其工作内存中
     *      所以程序正常运行。
     */
    private static void test01() {
        Data data = new Data();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "thread start...");
            try {
                TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {}

            data.changeData();
            System.out.println(Thread.currentThread().getName() + "thread end...");
            System.out.println("num:" + data.num);
        }, "A").start();

        while (data.num == 0) {

        }
        System.out.println("test end");
    }
}
