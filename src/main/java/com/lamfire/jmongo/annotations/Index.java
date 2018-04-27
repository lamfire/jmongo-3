


package com.lamfire.jmongo.annotations;


import java.lang.annotation.*;



@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Index {

    Field[] fields() default {};


    IndexOptions options() default @IndexOptions();


    @Deprecated
    boolean background() default false;


    @Deprecated
    boolean disableValidation() default false;


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
    String value() default "";

}
