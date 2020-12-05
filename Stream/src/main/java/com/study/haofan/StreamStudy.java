package com.study.haofan;

import javax.crypto.spec.PSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamStudy {

    public static void main(String args[]) {
        // 1 批量数据 -> Stream 对象
        Stream stream = Stream.of("admin", "tom", "damu");

        // 数组 -> Stream
        String [] strArrays = new String[] {"xueqi", "biyao"};
        Stream stream2 = Arrays.stream(strArrays);

        // List -> Stream
        List<String> list = new ArrayList<>();
        list.add("少林");
        list.add("武当");
        list.add("青城");
        Stream stream3 = list.stream();

        // Set -> Stream
        Set<String> set = new HashSet<>();
        set.add("少林罗汉拳");
        set.add("武当长拳");
        set.add("青城剑法");
        Stream stream4 = set.stream();

        // Map -> Stream
        Map<String, Integer> map = new HashMap<>();
        map.put("tom", 1000);
        map.put("jerry", 1200);
        map.put("shuke", 1000);
        Stream stream5 = map.entrySet().stream();

        // 2. Stream 对象对于基本数据类型的封装
        // int/long/double
        IntStream.of(new int[] {10, 20, 30}).forEach(System.out::println);
        IntStream.range(1, 5).forEach(System.out::println);

        // 3. Stream 对象 --> 转换成指定的数据类型
        // 数组
        Object[] objx = stream.toArray(String[]::new);

        // String
        // String str = stream.collect(Collectors.joining()).toString();

        // list
        // List<String> listx = (List<String>) stream.collect(Collectors.toList());

        // Set
        // Set<String> setx = (Set<String>) stream.collect(Collectors.toSet());

        // Map
        // Map<String, String> mapx = (Map<String, String>) stream.collect(Collectors.toMap(x->x, y->"value:"+y));

        // 4. Stream中常见的API操作
        List<String> accountList = new ArrayList<>();
        accountList.add("xongjiang");
        accountList.add("lujunyi");
        accountList.add("wuyong");

        // map()中间操作，map()方法接收一个Functional 接口
        accountList = accountList.stream().map(x -> "梁山好汉：" + x).collect(Collectors.toList());
        accountList.forEach(System.out::println);
        accountList.forEach(System.out::println);

        // filter 过滤
        accountList = accountList.stream().filter(x -> x.length() > 8).collect(Collectors.toList());
        accountList.forEach(System.out::println);

        // peak() 中间操作，多次迭代数据完成数据的处理过程
        accountList.stream()
                .peek(x -> System.out.println("Peak 1: " + x))
                .peek(x -> System.out.println("Peak 2: " + x))
                .forEach(System.out::println);

        // Stream中对数字运算的支持
        List<Integer> intList = new ArrayList<>();
        intList.add(20);
        intList.add(19);
        intList.add(7);
        intList.add(8);

        // skip()，有状态，跳过部分数据
        intList.stream().skip(3).forEach(System.out::println);

        // limit() 中间操作，有状态，限制输出数据量
        intList.stream().skip(2).limit(1).forEach(System.out::println);

        // distinct() 中间操作，剔除重复的数据
        intList.stream().distinct().forEach(System.out::println);

        // sorted() 中间操作，有状态，排序
        // max() 、min(),
        Optional optional1 = intList.stream().max((x, y) -> x -y );
        System.out.println(optional1.get());

        // reduce()：合并处理数据
        Optional optional2 = intList.stream().reduce((sum, x) -> sum + x);
        System.out.println(optional2.get());
    }
}
