package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.ObjectFactory;
import com.lamfire.jmongo.annotations.Version;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

import static java.lang.String.format;



public class VersionMisuse extends FieldConstraint {

    private ObjectFactory creator;


    public VersionMisuse(final ObjectFactory creator) {
        this.creator = creator;
    }

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        if (mf.hasAnnotation(Version.class)) {
            final Class<?> type = mf.getType();
            if (Long.class.equals(type) || long.class.equals(type)) {

                //TODO: Replace this will a read ObjectFactory call -- requires Mapper instance.
                final Object testInstance = creator.createInstance(mc.getClazz());

                // check initial value
                if (Long.class.equals(type)) {
                    if (mf.getFieldValue(testInstance) != null) {
                        ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                                       format("When using @%s on a Long field, it must be initialized to null.",
                                                              Version.class.getSimpleName())));
                    }
                } else if (long.class.equals(type)) {
                    if ((Long) mf.getFieldValue(testInstance) != 0L) {
                        ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                                       format("When using @%s on a long field, it must be initialized to 0.",
                                                              Version.class.getSimpleName())));
                    }
                }
            } else {
                ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                               format("@%s can only be used on a Long/long field.", Version.class.getSimpleName())));
            }
        }
    }

}
