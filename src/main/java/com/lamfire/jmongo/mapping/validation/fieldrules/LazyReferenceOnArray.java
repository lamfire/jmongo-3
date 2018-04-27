package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.annotations.Reference;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;



public class LazyReferenceOnArray extends FieldConstraint {

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        final Reference ref = mf.getAnnotation(Reference.class);
        if (ref != null && ref.lazy()) {
            final Class type = mf.getType();
            if (type.isArray()) {
                ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                               "The lazy attribute cannot be used for an Array. If you need a lazy array "
                                               + "please use ArrayList instead."));
            }
        }
    }

}
