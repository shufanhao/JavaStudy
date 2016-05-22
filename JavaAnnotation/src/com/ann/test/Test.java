package com.ann.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by haofan on 5/21/2016.
 */
public class Test {
   public static void main(String[] args){
       Filter f1 = new Filter();
       f1.setId(10);

       Filter f2 = new Filter();
       f2.setUserName("Lucy");

       Filter f3 = new Filter();
       f3.setEmail("liu@sina.com,zh@163.com,77@qq.com");

       String sql1 = query(f1);
       String sql2 = query(f2);
       String sql3 = query(f3);

       System.out.println(sql1);
       System.out.println(sql2);
       System.out.println(sql3);

       //换一个类，另外的一个表，也是同样可以打印出来sql语句
       Filter2 filter2 = new Filter2();
       filter2.setAmount(10);
       filter2.setName("技术部");
       String sql4 = query(filter2);
       System.out.println(sql4);


   }
    //不同的类都可以用这个方法进行拼装sql
    private static String query(Object f){
        StringBuffer sb = new StringBuffer();
        //1 反射获取到class
        Class c = f.getClass();//反射获取class
        //2 获取到table的名字
        boolean isExist = c.isAnnotationPresent(Table.class);
        if(!isExist){
            return null;
        }
        Table t = (Table)c.getAnnotation(Table.class);
        String tableName = t.value();
        sb.append(" select * from ").append(tableName).append(" where").append(" 1=1 ");
        //3 遍历所有的字段
        Field[] fArray = c.getDeclaredFields();
        for(Field field:fArray){
            //4 处理每个字段对应的sql
            //4.1 拿到字段名
            boolean fExist = field.isAnnotationPresent(Column.class);
            if(!fExist){
                continue;
            }
            Column column = (Column)field.getAnnotation(Column.class);
            String columnName = column.value();
            //4.2 拿到字段的值,通过方法的名字，反射取到方法
            String fieldName = field.getName();
            String getMethodName = "get"+ fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            Object fiedValue = null;
            try {
                Method getMethod = c.getMethod(getMethodName);//取到方法了
                try {
                    fiedValue = getMethod.invoke(f);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            //4.3 拼装sql
            if(fiedValue == null ||
                    (fiedValue instanceof  Integer && (Integer)fiedValue == 0)){
                continue;
            }
            sb.append(" and ").append(fieldName).append("=").append(fiedValue);
        }

        return  sb.toString();
    }
}
