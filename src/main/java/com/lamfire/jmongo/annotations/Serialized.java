


package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.mapping.Mapper;

import java.lang.annotation.*;



@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Serialized {


    boolean disableCompression() default false;


    String value() default Mapper.IGNORED_FIELDNAME;
}
