package com.ann.test;

import java.lang.reflect.Method;

/**
 * Created by haofan on 5/22/2016.
 */
public class ParseAnn {
    public static void main(String[] args){
        //1 使用类加载器加载类
        try {
            Class c = Class.forName("com.ann.test.Child");
            //2 找到类上面的注解
            boolean isExist = c.isAnnotationPresent(Desciption.class);
            if(isExist){
                //3 拿到注解,动态解析类的注解
                Desciption d = (Desciption)c.getAnnotation(Desciption.class);
                System.out.println(d.value());
            }
            // 4 找到方法的注解
            Method[] ms = c.getMethods();
            for(Method m:ms){
                isExist = m.isAnnotationPresent(Desciption.class);
                if(isExist){
                    Desciption d = m.getAnnotation(Desciption.class);
                    System.out.println(d.value());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
