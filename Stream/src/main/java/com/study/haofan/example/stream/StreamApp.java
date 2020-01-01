package com.study.haofan.example.stream;

import com.study.haofan.example.stream.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApp {
    public static void main(String[] args) {
        // 存储person对象
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("tom", "男", 16));
        personList.add(new Person("jerry", "女", 15));
        personList.add(new Person("shuke", "男", 30));

        // 1 sort by age 匿名内部类
        /*Collections.sort(personList, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        */
        // 2 sort by age lambda
        Collections.sort(personList, (o1, o2) -> o1.getAge() - o2.getAge());

        // 3 静态方法引用
        Collections.sort(personList, Person::compareByAge);
        // 4 实例方法引用
        // 5 构造方法引用
        System.out.println(personList);

        // 1. 添加测试数据：存储多个账号的列表
        List<String> accounts = new ArrayList<String>();
        accounts.add("tom");
        accounts.add("jerry");
        accounts.add("beita");
        accounts.add("damu");
        // 传统方式可能就是迭代
        // Stream方式
        List validAccounts = accounts.stream().filter(account -> account.length() >= 5).collect(Collectors.toList());
        System.out.println(validAccounts);
    }
}
