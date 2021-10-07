package com.lgq.distribute.lock;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/14 0014 12:48
 * @Version 1.0
 */
public interface ZKLock {
    public void zklock();
    public void zkUnlock();
}
