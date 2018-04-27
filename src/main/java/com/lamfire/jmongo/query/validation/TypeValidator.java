package com.lamfire.jmongo.query.validation;

import java.util.List;


public abstract class TypeValidator implements Validator {

    public boolean apply(final Class<?> type, final Object value,
                         final List<ValidationFailure> validationFailures) {

        if (appliesTo(type)) {
            validate(type, value, validationFailures);
            return true;
        }
        return false;
    }


    protected abstract boolean appliesTo(final Class<?> type);

    protected abstract void validate(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures);

}
