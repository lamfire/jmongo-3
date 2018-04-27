package com.lamfire.jmongo.mapping.validation.classrules;


import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ClassConstraint;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;



public class EmbeddedAndId implements ClassConstraint {

    @Override
    public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
        if (mc.getEmbeddedAnnotation() != null && mc.getIdField() != null) {
            ve.add(new ConstraintViolation(Level.FATAL, mc, getClass(),
                                           "@" + Embedded.class.getSimpleName() + " classes cannot specify a @Id field"));
        }
    }

}
