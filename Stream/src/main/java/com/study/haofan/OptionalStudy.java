package com.study.haofan;

import java.util.Optional;

public class OptionalStudy {
    public static void main(String args[]) {
        // Optional 目的：减少代码中的NullPointerException, 更方便的流式调用

        Optional<String> optional1 = Optional.of("Hao");
        Optional<String> optional2 = Optional.ofNullable(null);
        Optional<String> optional3 = Optional.empty();

        System.out.println(optional1.isPresent());
        System.out.println(optional2.isPresent());
        System.out.println(optional1.get());

        optional1.ifPresent(value -> System.out.println(value));
        System.out.println(optional2.orElse("null value"));
        try {
            optional3.orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<String> optional4 = optional1.map(value -> value.toUpperCase());
        System.out.println(optional4.get());

        Optional<String> optional5 = optional1.filter(value -> value.length() > 3);
        System.out.println(optional5.orElse("the length of value is less than 3"));
    }
}
