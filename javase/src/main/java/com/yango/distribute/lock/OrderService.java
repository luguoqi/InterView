package com.yango.distribute.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 12:25
 * @Version 1.0
 */
public class OrderService {
    private OrderNumCreateUtil orderNumCreateUtil = new OrderNumCreateUtil();
    private ZKDistributeLock zkDistributeLock = new ZKDistributeLock();

    public String getOrderNumber() {
        zkDistributeLock.zklock();
        try {
            return orderNumCreateUtil.getOrderNumber();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkDistributeLock.zkUnlock();
        }
        return "";
    }









//    private Lock lock = new ReentrantLock();

//    public String getOrderNumber() {
//        lock.lock();
//        try {
//            return orderNumCreateUtil.getOrderNumber();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//        return null;
//    }
}
