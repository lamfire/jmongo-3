package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;

import java.util.List;

import static java.lang.String.format;


public final class EntityTypeAndIdValueValidator implements Validator {
    private static final EntityTypeAndIdValueValidator INSTANCE = new EntityTypeAndIdValueValidator();

    private EntityTypeAndIdValueValidator() {
    }
    //TODO: I think this should be possible with the MappedField, not the type


    public static EntityTypeAndIdValueValidator getInstance() {
        return INSTANCE;
    }


    public boolean apply(final MappedClass mappedClass, final MappedField mappedField, final Object value,
                         final List<ValidationFailure> validationFailures) {
        if (appliesTo(mappedClass, mappedField)) {
            Class classOfValue = value.getClass();
            Class classOfIdFieldForType = mappedClass.getMappedIdField().getConcreteType();
            if (!mappedField.getType().equals(classOfValue) && !classOfValue.equals(classOfIdFieldForType)) {
                validationFailures.add(new ValidationFailure(format("The value class needs to match the type of ID for the field. "
                                                                    + "Value was %s and was a %s and the ID of the type was %s",
                                                                    value, classOfValue, classOfIdFieldForType)));
            }
            return true;
        }
        return false;
    }

    private boolean appliesTo(final MappedClass mappedClass, final MappedField mappedField) {
        return mappedField != null && mappedField.equals(mappedClass.getMappedIdField());
    }
}
