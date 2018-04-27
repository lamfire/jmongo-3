package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.annotations.Property;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;



public class MisplacedProperty extends FieldConstraint {

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        // a field can be a Value, Reference, or Embedded
        if (mf.hasAnnotation(Property.class)) {
            // make sure that the property type is supported
            if (mf.isSingleValue() && !mf.isTypeMongoCompatible() && !mapper.getConverters().hasSimpleValueConverter(mf)) {
                ve.add(new ConstraintViolation(Level.WARNING, mc, mf, getClass(),
                                               mf.getFullName() + " is annotated as @" + Property.class.getSimpleName()
                                               + " but is a type that cannot be mapped simply (type is "
                                               + mf.getType().getName() + ")."));
            }
        }
    }

}
