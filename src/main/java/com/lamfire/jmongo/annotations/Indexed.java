package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.utils.IndexDirection;

import java.lang.annotation.*;



@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Indexed {

    IndexOptions options() default @IndexOptions();


    boolean background() default false;


    boolean dropDups() default false;


    int expireAfterSeconds() default -1;


    String name() default "";


    boolean sparse() default false;


    boolean unique() default false;


    IndexDirection value() default IndexDirection.ASC;
}
