package com.lgq.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//资源类
class Share {
    private final Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private int num = 0;

    public void incr() throws Exception {
//        上锁
        lock.lock();
        try {
//        这里如果用if，可能会存在虚假唤醒
            while (num != 0) {
                condition.await();//在哪里等待就会在哪里唤醒，也就是说当前的线程可以唤醒当前线程从这里开始运行
            }
            num++;
            System.out.println(Thread.currentThread().getName() + "--" + num);
            condition.signalAll();
        } finally {
//            解锁
            lock.unlock();
        }
    }

    public void decr() throws Exception {
//        上锁
        lock.lock();
        try {
//        这里如果用if，可能会存在虚假唤醒
            while (num != 1) {
                condition.await();//在哪里等待就会在哪里唤醒，也就是说当前的线程可以唤醒当前线程从这里开始运行
            }
            num--;
            System.out.println(Thread.currentThread().getName() + "--" + num);
            condition.signalAll();
        } finally {
//            解锁
            lock.unlock();
        }
    }
}
//生产者消费者
//  判断->通知->干活
public class LThreadDemo {
    public static void main(String[] args) {
        Share share = new Share();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-1-1").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-1-2").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-2-1").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-2-2").start();
    }
}
