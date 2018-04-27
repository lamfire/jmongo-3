package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.mapping.Mapper;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Embedded {


    Class<?> concreteClass() default Object.class;


    String value() default Mapper.IGNORED_FIELDNAME;
}
