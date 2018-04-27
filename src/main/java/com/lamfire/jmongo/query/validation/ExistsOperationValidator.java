package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;

import java.util.List;

import static com.lamfire.jmongo.query.FilterOperator.EXISTS;
import static java.lang.String.format;


public final class ExistsOperationValidator extends OperationValidator {
    private static final ExistsOperationValidator INSTANCE = new ExistsOperationValidator();

    private ExistsOperationValidator() {
    }


    public static ExistsOperationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected FilterOperator getOperator() {
        return EXISTS;
    }

    @Override
    protected void validate(final MappedField mappedField, final Object value, final List<ValidationFailure> validationFailures) {
        if (value.getClass() != Boolean.class) {
            validationFailures.add(new ValidationFailure(format("For an $exists operation, value '%s' should be a boolean type.  "
                                                                + "Instead it was a: %s", value, value.getClass())));
        }
    }
}
