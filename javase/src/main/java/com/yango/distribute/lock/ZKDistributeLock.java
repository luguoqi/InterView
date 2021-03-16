package com.yango.distribute.lock;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 14:39
 * @Version 1.0
 */
public class ZKDistributeLock extends ZKAbstractTemplateLock {

    public void waitZkLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {

            }

            public void handleDataDeleted(String s) throws Exception {
                if (countDownLatch != null)
                    countDownLatch.countDown();
            }
        };
        zkClient.subscribeDataChanges(path, iZkDataListener);//添加监听
        if (zkClient.exists(path)) {//说明别人已经抢到锁，这里只能等待
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(path, iZkDataListener);
    }

    public boolean tryZkLock() {
        try {
            zkClient.createEphemeral(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
