package com.lamfire.jmongo.annotations;


import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EntityListeners {

    Class<?>[] value();
}
