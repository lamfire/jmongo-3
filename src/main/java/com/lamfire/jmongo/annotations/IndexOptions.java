


package com.lamfire.jmongo.annotations;


import java.lang.annotation.*;


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface IndexOptions {

    boolean background() default false;


    boolean disableValidation() default false;


    @Deprecated
    boolean dropDups() default false;


    int expireAfterSeconds() default -1;


    String language() default "";


    String languageOverride() default "";


    String name() default "";


    boolean sparse() default false;


    boolean unique() default false;


    String partialFilter() default "";


    Collation collation() default @Collation(locale = "");
}
