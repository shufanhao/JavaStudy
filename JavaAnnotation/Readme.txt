此Java 工程描述了Java 注解的应用，测试文件在test中，相关视频参考http://www.imooc.com/learn/456 
----------
[toc]
## Java 中常见注解
1. JDK常见注解。Override(覆盖父类或者接口的方法)，@Deprecated(表示该方法已经过时)，子类不用是实现这个方法。
2. Spring中的第三方注解，@autowired, @Service， @Repository等
## 自定义注解
- 使用@interface定义注解

```
@Target({ElementType.METHOD, ElementType.TYPE}) // 表示用在哪些作用域
@Retention(RetentionPolicy.RUNTIME) //表示注解的生命周期
@Inherited //允许子类继承
@Documented // 生成Java doc时，生成注解的信息
public @interface Description {
    String desc(); // 成员以无参无异常。如果注解只有一个成员 ，成员必须取名为value
    String author();
    int age() default 18; //可以用default为成员指定一个默认值
}
```
## 解析注解
通过反射获取类，函数或成员上的运行时注解信息，从而动态控制程序运行的逻辑

```
Description d = m.getAnnotaion(Desciption.class);
d.value();

```
