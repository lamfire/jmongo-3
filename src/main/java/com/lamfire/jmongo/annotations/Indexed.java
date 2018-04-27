package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.utils.IndexDirection;

import java.lang.annotation.*;



@SuppressWarnings("deprecation")
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Indexed {

    IndexOptions options() default @IndexOptions();


    @Deprecated
    boolean background() default false;


    @Deprecated
    boolean dropDups() default false;


    @Deprecated
    int expireAfterSeconds() default -1;


    @Deprecated
    String name() default "";


    @Deprecated
    boolean sparse() default false;


    @Deprecated
    boolean unique() default false;


    @Deprecated
    IndexDirection value() default IndexDirection.ASC;
}
