package com.ann.test;

import java.lang.annotation.*;

/**
 * Created by haofan on 5/22/2016.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Desciption {
    String value();
}
