package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.mapping.Mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Version {

    String value() default Mapper.IGNORED_FIELDNAME;
}
