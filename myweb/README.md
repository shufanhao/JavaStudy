# Spring Boot
Sping Boot是由Microservice 发展而来的，每个服务，高内聚低耦合。Spring boot是基于Spring，减少开发难度。Sping 的配置比较麻烦。
## hello world
http://start.spring.io/ select web
## Rest Controller
小技巧：
1. use RESTFUL web service with Spring Data REST
2. 修改port, 可以启动jar的时候修改，也可以修改，application.properties， 添加server.port = 8082
3. spring boot 默认是用tomcat：
就是下面的xml，另外可以改成jersy
```
<dependency>
<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
4. Spring boot监控
```
<dependency>
<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
可以用:
http://localhost:8080/beans  

http://localhost:8080/mappings

change log level use rest api:

```
endpoints.loggers.sensitive=false
endpoints.loggers.enabled=true
```

5. 接java console
```
<dependency>
<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-remote-shell</artifactId>
</dependency>
```
## Anotaion 
1. DependsOn
@DependsOn("ServicesStartupComplete")
保证该bean依赖的其他bean已经初始化
2. Component,Repository,Service,Controller


```
@Component | generic stereotype for any Spring-managed component |
| @Repository| stereotype for persistence layer                    |
| @Service   | stereotype for service layer                        |
| @Controller| stereotype for presentation layer (spring-mvc)
```
3. Autowired and resource

@Autowired与@Resource用法基本相同，区别在于@Autowired是默认以类型去查找，而@Resource默认以字段名去查找。
 
@Autowired(required=false)当根据类型去找，找不到时，注入一个空。

@Autowired(required=true)当根据类型去找，找不到时，抛出异常信息。

4. ComponentScan

在对我们的类进行元注解以后，我们现在需要将它们加入到Spring上下文，这是使用 @Configuration class by @ComponentScan
