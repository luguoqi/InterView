package com.yango.distribute.lock;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 12:49
 * @Version 1.0
 */
public abstract class ZKAbstractTemplateLock implements ZKLock {
    private static final String ZKSERVER = "192.168.136.134:2181";
    private static final int TIME_OUT = 45;
    public static final String path = "/lock";
    protected CountDownLatch countDownLatch = null;

    ZkClient zkClient = new ZkClient(ZKSERVER, TIME_OUT);

    public void zklock() {
        if (tryZkLock()) {
            System.out.println(Thread.currentThread().getName() + "\t占用锁成功");
        } else {
            waitZkLock();
            zklock();
        }
    }

    public abstract void waitZkLock();

    public abstract boolean tryZkLock();

    public void zkUnlock() {
        if (zkClient != null) {
            zkClient.close();
        }
        System.out.println(Thread.currentThread().getName() + "\t释放锁成功");
        System.out.println();
        System.out.println();
    }
}
