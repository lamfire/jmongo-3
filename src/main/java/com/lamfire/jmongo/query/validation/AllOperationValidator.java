package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;

import java.util.List;

import static com.lamfire.jmongo.query.FilterOperator.ALL;
import static com.lamfire.jmongo.query.validation.CollectionTypeValidator.typeIsIterableOrArrayOrMap;
import static java.lang.String.format;


public final class AllOperationValidator extends OperationValidator {
    private static final AllOperationValidator INSTANCE = new AllOperationValidator();

    private AllOperationValidator() {
    }


    public static AllOperationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected FilterOperator getOperator() {
        return ALL;
    }

    @Override
    protected void validate(final MappedField mappedField, final Object value, final List<ValidationFailure> validationFailures) {
        if (value == null) {
            validationFailures.add(new ValidationFailure(format("For an $all operation, value cannot be null.")));
        } else if (!typeIsIterableOrArrayOrMap(value.getClass())) {
            validationFailures.add(new ValidationFailure(format("For an $all operation, value '%s' should be an array, "
                                                                + "an Iterable, or a Map.  Instead it was a: %s",
                                                                value, value.getClass()
                                                               )));
        }
    }

}
