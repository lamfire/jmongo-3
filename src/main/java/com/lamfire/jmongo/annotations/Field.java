package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.utils.IndexType;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Field {

    IndexType type() default IndexType.ASC;


    String value();


    int weight() default -1;
}
