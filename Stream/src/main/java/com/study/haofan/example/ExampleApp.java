package com.study.haofan.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ExampleApp {

    public static void main(String args[]) {
        // 1. Predicate
        Predicate<String> pre = (String username) -> {
             return "admin".equals(username);
        };
        System.out.println(pre.test("admin"));

        // 2. Consumer
        Consumer<String> con = (String message) -> {
            System.out.println("要发送的消息：" + message);
            System.out.println("消息发送完成");
        };
        con.accept("hello");

        // 3. Function, pass T, return R
        Function<String, Integer> fun = (String gender) -> {
            return "male".equals(gender) ? 1:0;
        };
        System.out.println(fun.apply("male"));

        // 4. Supplier
        Supplier<String> sup = () -> {
            return UUID.randomUUID().toString();
        };
        System.out.println(sup.get());
        System.out.println(sup.get());

        // 5. UnaryOperator
        UnaryOperator<String> uo = (String img) -> {
            img += "123";
            return img;
        };
        System.out.println(uo.apply("原图"));

        // 类型检查
        test(new MyInterface<String, List>() {
            @Override
            public List strategy(String s, List list) {
                list.add(s);
                return list;
            }
        });
        // lambda 表达式写法
        // (x, y) -> {...} -> test(param) -> param=MyInterface -> lambda表达式，其实还是是对函数式接口的实现
        // JVM 会自动推测类型参数检查等
        test((x, y) -> {
            y.add(x);
            return y;
        });
    }

    @FunctionalInterface
    interface MyInterface<T, R> {
        R strategy(T t, R r);
    }

    public static void test(MyInterface<String, List> inter) {
        List<String> list = inter.strategy("hello", new ArrayList());
        System.out.println(list);
    }

}
