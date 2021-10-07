package com.lgq;

class Father {
    public Father() {
        System.out.println("1111111111");
    }
    {
        System.out.println("2222222222");
    }
    static {
        System.out.println("3333333333");
    }
}
class Son extends Father {
    public Son() {
        super();//默认会调用父类无参构造器
        System.out.println("4444444444");
    }
    {
        System.out.println("5555555555");
    }
    static {
        System.out.println("6666666666");
    }
}
public class TestStaticSeq {
    public static void main(String[] args) {
        new Son();
    }
}
