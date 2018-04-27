package com.lamfire.jmongo.annotations;

import com.lamfire.jmongo.mapping.Mapper;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Entity {

    CappedAt cap() default @CappedAt(0);


    String concern() default "";


    //@Deprecated //to be replaced. This is a temp hack until polymorphism and discriminators are implemented
    boolean noClassnameStored() default true;


    boolean queryNonPrimary() default true;


    String value() default Mapper.IGNORED_FIELDNAME;
}

