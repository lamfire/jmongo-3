package com.lamfire.jmongo.annotations;

import java.lang.annotation.*;


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Text {

    IndexOptions options() default @IndexOptions;


    int value() default -1;
}
