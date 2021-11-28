# JUC-5

集合的线程安全问题

### List集合并发问题

```java
public class CollectionThreadDemo {
//  演示list集合并发问题
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
//        方式一
//        List<String> list = new Vector<>();
//		  方式二
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,6));
//                输出集合内容，
//                使用new ArrayList<>()时可能会抛出java.util.ConcurrentModificationException
                System.out.println(list);
            }).start();
        }
    }
}
```

![在这里插入图片描述](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252213833.png)

![image-20210925225933823](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252259873.png)

![image-20210925225942476](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252259515.png)

**ConcurrentModificationException**异常产生的原因：因为这30个线程使用的是同一个list，同一个list里有同一个modCount，但每个线程在打印list的时候，都会Iterator<E> it = new Itr();,并且int expectedModCount = modCount;，每个线程都产生一个it和其中的expectedModCount 。如果A线程已经做完了Iterator<E> it = new Itr();,和int expectedModCount = modCount;，然后时间片用完了，B线程获得了CPU，B线程执行了list.add(String e)使得modCount++;，然后，当A线程再次获得CPU的时候，B线程在打印list的过程中，执行了it.next();方法，然后进入了checkForComodification()方法，发现modCount != expectedModCount（因为modCount已经被A线程增大了），于是抛出ConcurrentModificationException。
参考：https://blog.csdn.net/wpw2000/article/details/115265271

解决方案：

### 1.Vector

[List<String> list = new Vector<>();]

![image-20210925225841691](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252258745.png)

![image-20210925225852419](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252258458.png)

通过查看源码可以看到，Vector的add方法上加了同步锁。

```java
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}
```

### 2.Collections

[List<String> list = Collections.synchronizedList(new ArrayList<>());]

类似于Vector，不推荐使用

![image-20210925230042458](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252300525.png)

通过查看源码可知，其返回了一个自己实现的List，并且List中的所有操作都加了同步锁

![image-20210925231223889](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252312034.png)

### 3.CopyOnWriteArrayList

 (写时复制，推荐) [List<String> list = new CopyOnWriteArrayList<>();]

　　CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。

![image-20210925231326438](https://gitee.com/lugq_zh/images/raw/master/img-dd/202109252313506.png)

### Set集合并发问题

```java
public class CollectionThreadDemo {
//  演示set集合并发问题
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
//        解决：
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
//        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,6));
//                输出集合内容，可能会抛出java.util.ConcurrentModificationException
                System.out.println(set);
            }).start();
        }
    }
}
```

HashSet底层就是用HashMap，都是线程不安全的集合

### Map集合并发问题

```java
public class CollectionThreadDemo {
//  演示map集合并发问题
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
//		解决：
//		Hashtable
//		Collections.synchronizedSet(new HashMap<>())
//		ConcurrentMap
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                map.put(UUID.randomUUID().toString().substring(0,6), "test");
//                输出集合内容，可能会抛出java.util.ConcurrentModificationException
                System.out.println(map);
            }).start();
        }
    }
}
```



































































