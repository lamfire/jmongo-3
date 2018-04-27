


package com.lamfire.jmongo.annotations;


import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;

import java.lang.annotation.*;

import static com.mongodb.client.model.ValidationAction.ERROR;
import static com.mongodb.client.model.ValidationLevel.STRICT;



@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Validation {

    String value();


    ValidationLevel level() default STRICT;


    ValidationAction action() default ERROR;

}
