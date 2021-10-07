package com.lgq.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionThreadDemo {
//  演示list集合并发问题
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,6));
//                输出集合内容，可能会抛出java.util.ConcurrentModificationException
                System.out.println(set);
            }).start();
        }



    }

    private static void listTest() {
        //        List<String> list = new ArrayList<>();
//        List<String> list = new Vector<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,6));
//                输出集合内容，可能会抛出java.util.ConcurrentModificationException
                System.out.println(list);
            }).start();
        }
    }
}
