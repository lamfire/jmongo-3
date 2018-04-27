package com.lamfire.jmongo.query.validation;

import java.util.List;


public abstract class ValueValidator implements Validator {

    public boolean apply(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures) {
        if (getRequiredValueType().isAssignableFrom(value.getClass())) {
            validate(type, value, validationFailures);
            return true;
        }
        return false;
    }


    protected abstract Class<?> getRequiredValueType();

    protected abstract void validate(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures);
}
