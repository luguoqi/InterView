# JUC-2

锁介绍

## Synchronized

它是java的一个关键字，是一种同步锁。并且不能被继承，如果在父类中的某个方法使用了synchronized，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，必须显示的在这个方法上加synchronized

它可以修饰：

1.普通方法，锁对象是当前实例对象(即调用这个方法的对象)，synchronized void method() {  *//业务代码* }

2.静态方法，锁对象是当前类的Class对象，synchronized static void method() {  *//业务代码* }

3.代码块，可修饰静态和非静态代码块，synchronized(this) {  *//业务代码* }或者static synchronized(this) {  *//业务代码* }

经典单例实现：

```java
public class Singleton {
    //保证有序，防止指令重排
    private volatile static Singleton instance;
    private Singleton() {}
    public  static Singleton getInstance() {
        //先判断对象是否已经实例过，DCL
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

synchronized 同步原理：Java 虚拟机中的同步是基于进入和退出管程(Monitor)对象。synchronized 同步同步块的实现使用的是monitorenter和monitorexit，而monitorenter是同步代码块的开始位置，monitorexit是同步代码块的结束位置。当执行 monitorenter指令时，线程试图获取锁也就是获取 对象监视器 monitor 的持有权。此时锁的计数器设置为1(默认0)，持有锁的线程多次进入同步代码块时，锁的计数器累加(可重入锁)。当执行monitorenter指令后，将锁的计数器设置为0，表明锁释放。

![image-20210912193910451](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109121939818.png)

注意上面有一个monitorenter和两个monitorexit，其中一个是正常退出，一个是异常退出，也就是说不论同步代码块执行是否正常，一个monitorenter都会对应一个monitorexit。

卖票例子：

```java
//编程套路：线程->操作->资源类
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
```

## Lock

```java
package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;
public interface Lock {
    //阻塞获取锁，效果类似于synchronized，一般在finally中放锁
    void lock();
    //中断等待获取锁或者已经获取锁的线程
    void lockInterruptibly() throws InterruptedException;
    //尝试获取锁，返回是否获取锁成功
    boolean tryLock();
    //在tryLock()的基础上指定等待时间
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    //释放锁，一般放在finally中
    void unlock();
    //创建一个绑定到当前lock上的Condition
    Condition newCondition();
}
```

它是一个接口，可以通过其子类实现同步访问，但其加锁方所的过程是"手动"的，一般在finally释放锁，其提供了更为广泛的锁的操作，比如公平锁、非公平锁、可重入锁、读写锁等等，并且其可以感知是否获取到锁及设置等待锁的时长等等。

卖票例子：

```java
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
```

## Synchronized、Lock区别

Lock是接口，不是java语言内置的；synchronized是java的关键字，是java内置特性。

synchronized不需要手动释放锁，而Lock需要，如果没有主动释放可能导致死锁。

Lock有多种获取锁的方式，可以设置等待时间，设置公平锁非公平锁等，而synchronized只支持非公平锁，并且阻塞式获取锁，有jdk6只有被优化，有锁升级过程。

Lock可以返回是否成功获取到锁，并且可以判断锁的状态，并且可以中断；而synchronized都不可以。

Lock支持读写分离锁，并且可以通过Condition很方便的进行线程间调度，而synchronized只能使用Object对象的wait 、notify、notifyAll进行线程间调度。

Lock底层是通过CAS乐观锁，依赖AQS实现，而synchronized底层通过指令码方式控制锁，映射成两个指令monitorenter和monitorexit。

如果资源竞争不激烈，两者的性能是差不多的，但是当竞争资源非常激烈时Lock性能远优于synchronized

参考： https://blog.csdn.net/javazejian/article/details/72828483
