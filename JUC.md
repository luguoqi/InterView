# 并发编程

## 一、volatile

### 1.简介

​		volatile是java虚拟机提供的轻量级的同步机制，可以简单理解为它能保证多个线程操作同一变量时，能够获取到该变量的最新值，以避免脏读的出现。起作用为①保证可见性②**不保证原子性**③禁止指令重排序。

​		想要理解volatile，首先需要了解JMM(Java Memory Model，java内存模型)，他是一种理论模型。其规定1.线程放锁前，必须把共享变量的值刷回主内存。2.线程加锁前，必须读取主内存的最新值到自己的工作内存。3.加锁放锁是同一把锁。

​		由于JVM运行程序的本质是线程，每个线程创建时JVM都会为其创建一个工作内存，工作内存是每个线程的私有数据区域，而Java内存模型中所有变量都存储在主内存中，主内存是共享内存区域，所有线程都可以访问，**但线程对变量的操作(读取赋值等)，必须在工作内存中进行**，首先要将变量从主内存拷贝到自己的工作内存中，然后对变量进行操作，操作完成后再将变量写回主内存。而不能直接操作主内存中的变量，各个线程中的工作内存中存储着主内存中的变量副本拷贝，因此不同的线程间无法访问对方的工作内存，线程间通信必须通过主内存来完成，其过程如下：

![image-20200726223438758](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200726223438758.png)

#### 1.1 volatile 可见性

​		可见性是指当多个线程访问同一个变量时，一个线程修改了变量的值，其他线程能够立即看得到修改的值。即A、B线程虽然同时将同一个变量从主内存加载到各自的工作内存中，但当其中一个线程修改了变量的值，会通知其他线程重新从主内存中加载这个变量最新值到各自的工作内存中。

```java
import java.util.concurrent.TimeUnit;

class Data {
//    public  int num = 0;
    public volatile int num = 0;
    public void changeData() {
        num = 99;
    }
}
/**
 * volatile 可见性测试(本例使用两个线程，即main和A线程)
 *  情况1.变量num未添加  volatile 启动main线程卡死
 *  情况2.变量num添加 volatile 启动main线程，可正常执行结束
 *  结论：未添加volatile，A线程执行 3s后 先将变量num的值拷贝至线程的工作内存，然后修改其值为99，
 *		之后再将其值写回主物理内存，而在A线程拷贝num值之前，main线程已经将num值拷贝至其物理内存中，
 *      所以当A线程修改过num值之后，main线程并未重新将拷贝num值，从而导致main线程中的num值永远为0，所以出现死循环。
 *      添加volatile，由于volatile能保证可见性，当A线程改变了num值之后会通知main线程，此时main线程会重新从主内存
 *		中拷贝最新值至其工作内存中，所以程序正常运行。
 */
class VolatileTest {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "thread start...");
            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {}//A线程睡3s
            data.changeData();
            System.out.println(Thread.currentThread().getName() + "thread end...");
            System.out.println("num:" + data.num);
        }, "A").start();
        while (data.num == 0) {

        }
        System.out.println("test end");
    }
}
```

​                                                                                                                                                                                                                                                                                                                                                 

#### 1.2 volatile 原子性

​		指一个操作是不可中断的，即使是在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程干扰，在这里可以理解为，对变量的操作(①从主内存加载至工作内存、②操作、③从工作内存刷新至主内存)，一旦开始，就不会被其他线程打断。

```java
class Data {
    public volatile int num = 0;
    //volatile 不保证原子性
    public void add() {
        num++;			//	--->对应字节码指令：aload_0、iadd、putfield
    }
}
class VolatileTest {
    /**
     * volatile 原子性测试(不保证原子性)
     */
    public static void main(String[] args) {
        Data data = new Data();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    data.add();
                }
            }, "thread" + i ).start();
        }
        //main、GC线程外还有活跃线程就等待
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        //最终结果理论上应该是20000，但实际上却小于此值
        System.out.println(data.num);
    }
```

![image-20200809172358948](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200809172358948.png)

​		如图所示，我们知道在JMM中i++并不是原子操作，i++其实有三步操作1.加载i值到工作内存2.进行+1并赋值3.将+1后的值刷新至主内存，由于不是原子性操作就会存在，线程A和线程B同时将i=0加载至各自工作内存，然后同时对i进行加1操作并复制，由于此时两个线程都还没写入主内存，所以就不存在可见性，如果此时两个线程同时将i值写回主内存，此时就会有一个线程的值覆盖另一个线程的值。

解决：

​		1.添加 synchronized 关键字

```java
class Data {
    public volatile int num = 0;
    public synchronized void add() {//添加synchroized保证原子性，其实此时就不需要使用volatile了
        num++;
    }
}
class VolatileTest {
    public static void main(String[] args) {
        Data data = new Data();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    data.add();
                }
            }, "thread" + i ).start();
        }
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(data.num);//20000
    }
```

​		2.使用 atomic相关类

```java
class Data {
    public AtomicInteger atomicInteger = new AtomicInteger();
    public void add() {
        atomicInteger.getAndIncrement();
    }
}
class VolatileTest {
    /**
     * AtomicInteger(CAS) 原子性测试
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
        System.out.println(data.atomicInteger);//20000
    }
```

#### 1.3 volatile 有序性

​		计算机在执行程序时，为了提高性能，编译器和处理器常常会对执行进行重排，在单线程中这种重排不会影响程序执行结果，但是在多线程环境中指令重排就可能导致意想不到的结果，如图所示，声明的顺序是1234，但执行时可能是1234、2134、1324等，而volatile实现禁止指令重排优化，从而避免在多线程环境下出现乱序执行的现象

![image-20200809212827026](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200809212827026.png)

指令重排问题1：如图所示，指令重排后x,y的值可能不确定(添加volatile禁止指令重排)

![image-20200809213707756](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200809213707756.png)

指令重排问题2：

```java
public class ReSortSeqDemo {

    int a = 0;
    boolean flag = false;
    public void test1() {
        a = 1;//语句1
        flag = true;//语句2
    }
	/**
	 * 多线程环境中，由于编译器优化重排的存在，两个线程使用的变量能否保证一致性是无法确定的，结果无法预测
	 * 也就是说在多线程环境中语句1和语句2是没有数据依赖性的，可能会出现先执行语句2，在执行语句1，这时候可能会
	 * 出现打印的a的值为5的情况，同理也可能出现最终a值为6的情况。
	 *	解决：添加volatile关键字，禁止指令重排序，这时候不管是在什么情况下，都是先执行语句1在执行语句2，a的最终
	 *  结果必定是6
     */
    public void test2() {
        if(flag) {
            a = a + 5;//语句3
            System.out.println("===" + a);
        }
    }
}
```

#### 1.4 volatile 应用

​		