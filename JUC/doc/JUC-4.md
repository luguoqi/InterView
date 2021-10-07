# JUC-4

线程间定制通信

### 案例：

A打印5次、B打印10次、C打印15次，整体打印10轮

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
//资源类
class Share1 {
    //    执行标志位
    private int flag = 1;
    private final Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();
    //    操作方法
    public void print5(int loop) throws InterruptedException {
//        加锁
        lock.lock();
        try {
//            判断
            while (flag != 1) {
                c1.await();
            }
//            干活
            System.out.println("------------------" + Thread.currentThread().getName() + "-----------------：" + loop + "轮");
            print(5);
//            通知
            flag = 2;
            c2.signal();
        } finally {
//            放锁
            lock.unlock();
        }
    }
    //    操作方法
    public void print10(int loop) throws InterruptedException {
//        加锁
        lock.lock();
        try {
//            判断
            while (flag != 2) {
                c2.await();
            }
//            干活
            System.out.println("------------------" + Thread.currentThread().getName() + "-----------------：" + loop + "轮");
            print(10);
//            通知
            flag = 3;
            c3.signal();
        } finally {
//            放锁
            lock.unlock();
        }
    }
    //    操作方法
    public void print15(int loop) throws InterruptedException {
//        加锁
        lock.lock();
        try {
//            判断
            while (flag != 3) {
                c3.await();
            }
//            干活
            System.out.println("------------------" + Thread.currentThread().getName() + "-----------------：" + loop + "轮");
            print(15);
//            通知
            flag = 1;
            c1.signal();
        } finally {
//            放锁
            lock.unlock();
        }
    }
    public void print(int num) {
        Stream.iterate(1, (x) -> x + 1).limit(num).forEach(System.out::println);
    }
}
public class ThreadDemo1 {
    public static void main(String[] args) {
        Share1 share1 = new Share1();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    share1.print5(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    share1.print10(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    share1.print15(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
    }
}
```
