package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ClassConstraint;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;

import java.util.Set;



public abstract class FieldConstraint implements ClassConstraint {
    @Override
    public final void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
        for (final MappedField mf : mc.getPersistenceFields()) {
            check(mapper, mc, mf, ve);
        }
    }

    protected abstract void check(final Mapper mapper, MappedClass mc, MappedField mf, Set<ConstraintViolation> ve);

}
