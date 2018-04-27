package com.lamfire.jmongo.mapping.validation.fieldrules;


import com.lamfire.jmongo.annotations.Serialized;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation;
import com.lamfire.jmongo.mapping.validation.ConstraintViolation.Level;
import com.lamfire.jmongo.utils.ReflectionUtils;
import org.bson.types.ObjectId;

import java.util.Set;



public class MapKeyDifferentFromString extends FieldConstraint {
    private static final String SUPPORTED = "(Map<String/Enum/Long/ObjectId/..., ?>)";

    @Override
    protected void check(final Mapper mapper, final MappedClass mc, final MappedField mf, final Set<ConstraintViolation> ve) {
        if (mf.isMap() && (!mf.hasAnnotation(Serialized.class))) {
            final Class<?> aClass = ReflectionUtils.getParameterizedClass(mf.getField(), 0);
            // WARN if not parameterized : null or Object...
            if (aClass == null || Object.class.equals(aClass)) {
                ve.add(new ConstraintViolation(Level.WARNING, mc, mf, getClass(),
                                               "Maps cannot be keyed by Object (Map<Object,?>); Use a parametrized type that is supported "
                                               + SUPPORTED));
            } else if (!aClass.equals(String.class) && !aClass.equals(ObjectId.class) && !ReflectionUtils.isPrimitiveLike(
                                                                                                                             aClass)) {
                ve.add(new ConstraintViolation(Level.FATAL, mc, mf, getClass(),
                                               "Maps must be keyed by a simple type " + SUPPORTED + "; " + aClass
                                               + " is not supported as a map key type."));
            }
        }
    }
}
