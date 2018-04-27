package com.lamfire.jmongo.mapping.validation.classrules;


import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ClassConstraint;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Map;
import java.util.Set;



public class EntityCannotBeMapOrIterable implements ClassConstraint {

    @Override
    public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {

        if (mc.getEntityAnnotation() != null && (Map.class.isAssignableFrom(mc.getClazz())
                                                 || Iterable.class.isAssignableFrom(mc.getClazz()))) {
            ve.add(new ConstraintViolation(Level.FATAL, mc, getClass(), "Entities cannot implement Map/Iterable"));
        }

    }
}
