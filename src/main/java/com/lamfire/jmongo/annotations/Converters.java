package com.lamfire.jmongo.annotations;

import com.lamfire.jmongo.converters.TypeConverter;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Converters {

    Class<? extends TypeConverter>[] value();
}
