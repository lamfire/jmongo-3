package com.lamfire.jmongo.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CappedAt {

    long count() default 0;


    long value() default 1024 * 1024;
}
