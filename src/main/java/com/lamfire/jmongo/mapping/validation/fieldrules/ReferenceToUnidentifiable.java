package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.annotations.Id;
import com.lamfire.jmongo.annotations.Reference;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.MappingException;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;



public class ReferenceToUnidentifiable extends FieldConstraint {

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        if (mf.hasAnnotation(Reference.class)) {
            final Class realType = (mf.isSingleValue()) ? mf.getType() : mf.getSubClass();

            if (realType == null) {
                throw new MappingException("Type is null for this MappedField: " + mf);
            }

            if ((!realType.isInterface() && mapper.getMappedClass(realType).getIdField() == null)) {
                ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                               mf.getFullName() + " is annotated as a @" + Reference.class.getSimpleName() + " but the "
                                               + mf.getType().getName()
                                               + " class is missing the @" + Id.class.getSimpleName() + " annotation"));
            }
        }
    }

}
