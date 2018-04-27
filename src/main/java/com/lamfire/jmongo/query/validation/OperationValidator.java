package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;

import java.util.List;


public abstract class OperationValidator implements Validator {

    public boolean apply(final MappedField mappedField, final FilterOperator operator, final Object value,
                         final List<ValidationFailure> validationFailures) {
        if (getOperator().equals(operator)) {
            validate(mappedField, value, validationFailures);
            return true;
        }
        return false;
    }


    protected abstract FilterOperator getOperator();


    protected abstract void validate(final MappedField mappedField, final Object value, final List<ValidationFailure> validationFailures);

}
