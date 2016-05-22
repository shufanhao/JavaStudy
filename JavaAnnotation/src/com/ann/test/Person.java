package com.ann.test;

/**
 * Created by haofan on 5/21/2016.
 */
public interface Person {
    public String name();
    public int age();
    @Deprecated    //这个方法过时了
    public void sing();
}
