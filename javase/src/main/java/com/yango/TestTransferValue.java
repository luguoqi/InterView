package com.yango;

/**
 * @Description TODO
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/3/10 0010 22:01
 * @Version 1.0
 */
public class TestTransferValue {

    private static int id = 3000;
    /**
     * 基本类型都是值传递，值在栈中
     */
    public void changeValue1(int age) {
        age = 30;
    }

    /**
     * 传递引用，此处person和main中person指向同一个对象
     */
    public void changeValue2(Person person) {
        person.setName("xxx");
    }

    /**
     * 字符串常量池，此处str和main中str指向同一个字符串，此处将str又重新指向了另一个新的字符串
     */
    public void changeValue3(String str) {
        str = "xxx-str";
    }
    /**
     * 基本类型都是值传递，值在栈中
     */
    public void changeValue4(int id) {
        id = 20;
    }
    /**
     * 此处person和main中person指向同一个对象，然后此处将str又重新指向了另一个新的对象
     */
    public void changeValue5(Person person) {
        person = new Person("newperson");
    }

    public static void main(String[] args) {
        TestTransferValue test = new TestTransferValue();
        test.changeValue4(id);
        System.out.println("id--" + id);
        int age = 20;
        test.changeValue1(age);
        System.out.println("age--" + age);
        Person person = new Person("abc");
        test.changeValue2(person);
        System.out.println("person.name---" + person.getName());
        String str = "abc";
        test.changeValue3(str);
        System.out.println("string--" + str);
        test.changeValue5(person);
        System.out.println("person.name---" + person.getName());

    }
}
class Person {
    private int age;
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}