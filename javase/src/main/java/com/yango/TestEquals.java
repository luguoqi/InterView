package com.yango;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/11 0011 21:41
 * @Version 1.0
 */
public class TestEquals {
    public static void main(String[] args) {
        String s1 = new String("abc");
        String s2 = new String("abc");
//      由于此处是new所以比较的是对象的地址值
        System.out.println(s1==s2);
//        此处因为string重写了equals方法，其比较的是值
        System.out.println(s1.equals(s2));
        System.out.println(s1.hashCode() + "\t" + s2.hashCode());
//        HashSet底层是HashMap，其使用hashcode来确定key所在hash表的位置，由于string重写了hashcode，所以此处的hashcode值一样，
//        导致key存储在hashmap中的相同位置的hash表中，产生hash碰撞，又因为两个值相等，所以发生覆盖
        Set<String> set1=new HashSet<>();
        set1.add(s1);
        set1.add(s2);
        System.out.println(set1.size());
        Person p1= new Person("abc");
        Person p2 = new Person("abc");
//        此处的person既没有复写equals也没有复写hashcode，所有==和equals比较的值都是false，又因为hashcode值不相同，所以存储在hashmap中时在不同的位置
        System.out.println(p1==p2);
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode() + "\t" + p2.hashCode());
        Set<Person> set2 = new HashSet<>();
        set2.add(p1);
        set2.add(p2);
        System.out.println(set2.size());
        System.out.println("=====================================================================================");
        String s3 = "abc";
        String s4 = new String("abc");
        String s5 = "abc";
        String s6 = "xxx";
        String s7 = "abc" + "xxx";
        String s8 = s5 + s6;
        System.out.println(s3 == s4);
        System.out.println(s3 == s7);
        System.out.println(s3 == s8);
//        因为s8在堆上创建了一个新的对象，而s7是在编译器已经确定了值，在常量池中创建的对象
        System.out.println(s7 == s8);
        /*
            返回字符创对象的规范表示形式
            字符串池最初为空，由类字符串私下维护，
            调用intern方法时，如果池中已包含equals(object)方法确定的与此string对象相等的字符串，则返回池中的字符串，否则，此字符串对象将添加到
            池中，并返回对此字符串对象的引用。
            因此，对于任意两个字符串s和t，s.intern()==t.intern()在且晋档s.equals(t)为true时为true，
            所有文字字符串和字符串值常量表达式都会被插入。String文字在Java语言规范的3.3.5节中定义
         */
        System.out.println(s3 == s8.intern());
        System.out.println(s4 == s4.intern());

    }
}
