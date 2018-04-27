package com.lamfire.jmongo.mapping.validation.classrules;


import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.annotations.Entity;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ClassConstraint;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;



public class EntityAndEmbed implements ClassConstraint {

    @Override
    public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {

        if (mc.getEntityAnnotation() != null && mc.getEmbeddedAnnotation() != null) {
            new ConstraintViolation(Level.FATAL, mc, getClass(),
                                    "Cannot have both @" + Entity.class.getSimpleName() + " and @" + Embedded.class.getSimpleName()
                                    + " annotation at class level.");
        }

    }
}
