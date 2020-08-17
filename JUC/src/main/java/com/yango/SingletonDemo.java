package com.yango;

import java.io.Serializable;

public class SingletonDemo implements Serializable {

    private SingletonDemo(){
        System.out.println("init constructor" + Thread.currentThread().getName());
    }

    private static volatile  SingletonDemo singletonDemo = null;

    //直接添加synchronized并发并不好，这里使用双端检索机制
    public static SingletonDemo getInstance() {
        if(singletonDemo == null) {
            synchronized (SingletonDemo.class) {
                if(singletonDemo == null) {
//                    初始化对象可以分为三个步骤
//                    memory=allocate();//1.分配内存空间
//                    instance(memory);//2.初始化对象
//                    instance=memeory;//3.设置instance指向刚分配的内存地址，此时instance!=null
//                    因为步骤2、3不存在数据依赖关系，所以其执行先后顺序不确定，这就可能出现，对象还没初始化完成，此时对象实例已经不是null了
                    singletonDemo = new SingletonDemo();
                }
            }
        }
        return singletonDemo;
    }

    public static void main(String[] args) {
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        for (int i = 0; i < 200; i++) {
            new Thread(()-> {
                System.out.println(SingletonDemo.getInstance());
            }, String.valueOf(i)).start();
        }


    }

}
