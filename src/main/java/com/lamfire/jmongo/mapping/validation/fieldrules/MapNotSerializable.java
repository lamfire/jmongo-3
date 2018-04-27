package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.annotations.Serialized;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;
import com.lamfire.jmongo.utils.ReflectionUtils;

import java.io.Serializable;
import java.util.Set;



public class MapNotSerializable extends FieldConstraint {

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        if (mf.isMap()) {
            if (mf.hasAnnotation(Serialized.class)) {
                final Class<?> keyClass = ReflectionUtils.getParameterizedClass(mf.getField(), 0);
                final Class<?> valueClass = ReflectionUtils.getParameterizedClass(mf.getField(), 1);
                if (keyClass != null) {
                    if (!Serializable.class.isAssignableFrom(keyClass)) {
                        ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                                       "Key class (" + keyClass.getName() + ") is not Serializable"));
                    }
                }
                if (valueClass != null) {
                    if (!Serializable.class.isAssignableFrom(valueClass)) {
                        ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                                       "Value class (" + valueClass.getName() + ") is not Serializable"));
                    }
                }
            }
        }
    }
}
