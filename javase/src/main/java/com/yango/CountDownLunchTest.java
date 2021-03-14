package com.yango;

import java.util.concurrent.CountDownLatch;

public class CountDownLunchTest {
    private CountDownLatch countDownLatch  = new CountDownLatch(5);

    public void doWork(){
        countDownLatch.countDown();
        System.out.println(countDownLatch.getCount());
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLunchTest test = new CountDownLunchTest();
        Runnable runnable = new Runnable() {
            public void run() {
                test.doWork();
            }
        };
        for (int i = 0; i <5; i++) {
            new Thread(runnable).start();
        }
        test.countDownLatch.await();
        System.out.println("end");
    }
}
