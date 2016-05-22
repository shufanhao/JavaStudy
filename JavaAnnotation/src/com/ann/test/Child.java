package com.ann.test;

/**
 * Created by haofan on 5/21/2016.
 */
@Desciption("I am class annotation")
public class Child implements Person {
    @Override
    @Desciption("I am  method annotation")
    public String name() {
        return null;
    }

    @Override
    public int age() {
        return 0;
    }

    @Override
    public void sing() {

    }
}
