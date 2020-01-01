# Lambda
##  1 What is lambda  and why 
#### 1.1 what
 - lambda 表达式称为箭头函数，匿名函数，闭包
 - lambda 表达式体现是的轻量级函数式编程思想
 - -> 符号lambda表达式核心操作符号，符号左侧是操作参数，右侧是操作表达式
 - Jdk新特性

### 1.2 Model code as data
编码及数据，尽可能轻量级的将代码封装为数据。通过接口&实现类（匿名内部类），但是存在语法冗余，this关键字等缺点。

### 1.3  why 
lambda解决的问题是把原来通过匿名内部类实现的方式，写成lambda的方式。
## 2 lambda 基础知识
### 2.1 函数式接口

 - 只包含一个接口方法的特殊接口，语义化检测注解：@FunctionalInterface
 - 可以有default 方法和静态方法
 ### 2.2 函数式接口和lambda表达式的关系
 
 - 函数式接口，只包含一个方法
 - Lambda表达式，只能操作一个方法
 - `Java中的lambda表达式，核心就是一个函数式接口的实现`, 重要！！！！！
 ### 2.2 jdk中常见的函数式接口
 
 - Runable
 - Comparable
 - Comparator
 - FileFilter
 - Predicate<T> 接受参数对象T，返回一个boolean结果
 
 Java.uti.function提供了大量的函数式接口
 - Predicate 接收参数T对象，返回一个boolean类型结果
 - Comsumer<T> 接收参数T，不返回结果 
 - Function 接收参数T对象，返回R对象
 - Supplier 不接收任何参数，直接通过get()获取指定类型的对象
 - UnaryOperator 接口参数T对象，执行业务处理后，返回更新后的T对象
 - BinaryOperator 接收两个T对象，执行业务处理后，返回一个T对象
 
  ### 2.3 lambda 基本语法
  1. 声明：就是和labmbda表达式绑定的接口类型
  2. 参数：包含在一对圆括号中，和绑定的接口中的抽象方法中的参数个数及顺序一致。
  3. 操作符: -> 执行代码块，包含在一对大括号中，出现在操作符的右侧
  
[接口声明] = (参数) -> {执行代码块}; 

lambda 可以自动推测参数类型，如果就一行代码，不写{}的情况下可以不写return
## 3 lambda 表达式在集合中的运用
### 3.1 方法引用
1. 静态方法引用。在接口中定义静态方法，然后通过 类型名称::方法名称的方式。
```
Collections.sort(personList, Person::compareByAge);
```
2. 实例方法引用
3. 构造方法引用
### 3.2 Stream 概述
1. 聚合操作
2. stream的处理流程
    数据源
    数据转换
    获取结果
3. 获取Stream对象
    1. 从集合或者数组中获取[**]
        Collection.stream()，如accounts.stream()
        Collection.parallelStream()
        Arrays.stream(T t)
    2. BufferReader
        BufferReader.lines()-> stream()
    3. 静态工厂
        java.util.stream.IntStream.range()..
        java.nio.file.Files.walk()..
    4. 自定构建
        java.util.Spliterator
    5. 更多的方式..
        Random.ints()
        Pattern.splitAsStream()..
 4. 中间操作API{intermediate}
    操作结果是一个Stream，中间操作可以有一个或者多个连续的中间操作，需要注意的是，中间操作
        只记录操作方式，不做具体执行，直到结束操作发生时，才做数据的最终执行。
        中间操作：就是业务逻辑处理。
    中间操作过程：无状态：数据处理时，不受前置中间操作的影响。
                    map/filter/peek/parallel/sequential/unordered
                有状态：数据处理时，受到前置中间操作的影响。
                    distinct/sorted/limit/skip
 5. 终结操作|结束操作{Terminal}
    需要注意：一个Stream对象，只能有一个Terminal操作，这个操作一旦发生，就会真实处理数据，生成对应的处理结果。
    终结操作：非短路操作：当前的Stream对象必须处理完集合中所有 数据，才能得到处理结果。
                forEach/forEachOrdered/toArray/reduce/collect/min/max/count/iterator
            短路操作：当前的Stream对象在处理过程中，一旦满足某个条件，就可以得到结果。
                anyMatch/allMatch/noneMatch/findFirst/findAny等
                Short-circuiting，无限大的Stream-> 有限大的Stream。
