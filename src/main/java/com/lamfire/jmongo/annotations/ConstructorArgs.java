package com.lamfire.jmongo.annotations;


import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConstructorArgs {

    String[] value();
}
