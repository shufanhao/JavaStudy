package com.study.haofan;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamStudy {

    public void filterDemo() {

    }
    public static void main(String args[]) {
        // 1 Stream filter
        List<String> list = Arrays.asList("java", "scala", "python", "shell", "ruby");
        long num = list.parallelStream().filter(x -> x.length() < 5).count();
        System.out.println(num);

        // 2 Stream map, 遍历原stream中的元素，转换后构成新的stream
        list = Arrays.asList(new String[] {"a", "b", "c"});
        List<String> result = list.parallelStream().map(String::toUpperCase).collect(Collectors.toList());
        result.forEach( x -> System.out.println(x));

        // TODO

    }
}
