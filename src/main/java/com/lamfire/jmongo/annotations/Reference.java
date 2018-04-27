


package com.lamfire.jmongo.annotations;


import com.lamfire.jmongo.mapping.Mapper;

import java.lang.annotation.*;



@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {

    @Deprecated Class<?> concreteClass() default Object.class;


    boolean idOnly() default false;


    boolean ignoreMissing() default false;


    boolean lazy() default false;


    String value() default Mapper.IGNORED_FIELDNAME;
}
