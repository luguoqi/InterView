# JUC-3

线程间通信

编程套路：判断->干活->通知

### wait

在其他线程调用此对象的 [`notify()`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/lang/Object.html#notify()) 方法或 [`notifyAll()`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/lang/Object.html#notifyAll()) 方法前，导致当前线程等待。换句话说，此方法的行为就好像它仅执行 `wait(0)` 调用一样。

当前线程必须拥有此对象监视器。该线程发布对此监视器的所有权并等待，直到其他线程通过调用 `notify` 方法，或 `notifyAll` 方法通知在此对象的监视器上等待的线程醒来。然后该线程将等到重新获得对监视器的所有权后才能继续执行。

对于某一个参数的版本，实现中断和虚假唤醒是可能的，而且此方法应始终在循环中使用：

```java
synchronized (obj) {
while (<condition does not hold>)
obj.wait();
... // Perform action appropriate to condition
     }
```

### notify

唤醒在此对象监视器上等待的单个线程。如果所有线程都在此对象上等待，则会选择唤醒其中一个线程。选择是任意性的，并在对实现做出决定时发生。线程通过调用其中一个 `wait` 方法，在对象的监视器上等待。

直到当前线程放弃此对象上的锁定，才能继续执行被唤醒的线程。被唤醒的线程将以常规方式与在该对象上主动同步的其他所有线程进行竞争；例如，唤醒的线程在作为锁定此对象的下一个线程方面没有可靠的特权或劣势。

此方法只应由作为此对象监视器的所有者的线程来调用。通过以下三种方法之一，线程可以成为此对象监视器的所有者：

- 通过执行此对象的同步实例方法。
- 通过执行在此对象上进行同步的 `synchronized` 语句的正文。
- 对于 `Class` 类型的对象，可以通过执行该类的同步静态方法。

一次只能有一个线程拥有对象的监视器。

### notifyAll

唤醒在此对象监视器上等待的所有线程。线程通过调用其中一个 `wait` 方法，在对象的监视器上等待。

直到当前线程放弃此对象上的锁定，才能继续执行被唤醒的线程。被唤醒的线程将以常规方式与在该对象上主动同步的其他所有线程进行竞争；例如，唤醒的线程在作为锁定此对象的下一个线程方面没有可靠的特权或劣势。

此方法只应由作为此对象监视器的所有者的线程来调用。有关线程能够成为监视器所有者的方法的描述，请参阅 `notify` 方法。

### 案例：

```java
package com.lgq;
//资源类
class Share {
    private int num = 0;
    public synchronized void incr() throws Exception {
//        这里如果用if，可能会存在虚假唤醒
        while (num != 0) {
            wait();//在哪里等待就会在哪里唤醒，也就是说当前的线程可以唤醒当前线程从这里开始运行
        }
        num++;
        System.out.println(Thread.currentThread().getName() + "--" + num);
        notifyAll();
    }
    public synchronized void decr() throws Exception {
//        这里如果用if，可能会存在虚假唤醒
        while (num != 1) {
            wait();//在哪里等待就会在哪里唤醒，也就是说当前的线程可以唤醒当前线程从这里开始运行
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "--" + num);
        notifyAll();
    }
}
//生产者消费者
//  判断->通知->干活
public class ThreadDemo1 {
    public static void main(String[] args) {
        Share share = new Share();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-1").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread-2").start();
    }
}
```

### Condition

`Condition` 将 `Object` 监视器方法（[`wait`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/lang/Object.html#wait())、[`notify`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/lang/Object.html#notify()) 和 [`notifyAll`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/lang/Object.html#notifyAll())）分解成截然不同的对象，以便通过将这些对象与任意 [`Lock`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/util/concurrent/locks/Lock.html) 实现组合使用，为每个对象提供多个等待 set（wait-set）。其中，`Lock` 替代了 `synchronized` 方法和语句的使用，`Condition` 替代了 Object 监视器方法的使用。

条件（也称为*条件队列* 或*条.0件变量*）为线程提供了一个含义，以便在某个状态条件现在可能为 true 的另一个线程通知它之前，一直挂起该线程（即让其“等待”）。因为访问此共享状态信息发生在不同的线程中，所以它必须受保护，因此要将某种形式的锁与该条件相关联。等待提供一个条件的主要属性是：*以原子方式* 释放相关的锁，并挂起当前线程，就像 `Object.wait` 做的那样。

`Condition` 实例实质上被绑定到一个锁上。要为特定 [`Lock`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/util/concurrent/locks/Lock.html) 实例获得 `Condition` 实例，请使用其 [`newCondition()`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/util/concurrent/locks/Lock.html#newCondition()) 方法。

作为一个示例，假定有一个绑定的缓冲区，它支持 `put` 和 `take` 方法。如果试图在空的缓冲区上执行 `take` 操作，则在某一个项变得可用之前，线程将一直阻塞；如果试图在满的缓冲区上执行 `put` 操作，则在有空间变得可用之前，线程将一直阻塞。我们喜欢在单独的等待 set 中保存 `put` 线程和 `take` 线程，这样就可以在缓冲区中的项或空间变得可用时利用最佳规划，一次只通知一个线程。可以使用两个 [`Condition`](https://tool.oschina.net/uploads/apidocs/jdk-zh/java/util/concurrent/locks/Condition.html) 实例来做到这一点。

```java
 class BoundedBuffer {
   final Lock lock = new ReentrantLock();
   final Condition notFull  = lock.newCondition(); 
   final Condition notEmpty = lock.newCondition(); 

   final Object[] items = new Object[100];
   int putptr, takeptr, count;

   public void put(Object x) throws InterruptedException {
     lock.lock();
     try {
       while (count == items.length) 
         notFull.await();
       items[putptr] = x; 
       if (++putptr == items.length) putptr = 0;
       ++count;
       notEmpty.signal();
     } finally {
       lock.unlock();
     }
   }

   public Object take() throws InterruptedException {
     lock.lock();
     try {
       while (count == 0) 
         notEmpty.await();
       Object x = items[takeptr]; 
       if (++takeptr == items.length) takeptr = 0;
       --count;
       notFull.signal();
       return x;
     } finally {
       lock.unlock();
     }
   } 
 }
```

### 案例：

```java
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
//生产者消费者，两个消费者两个生产者
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
```



























