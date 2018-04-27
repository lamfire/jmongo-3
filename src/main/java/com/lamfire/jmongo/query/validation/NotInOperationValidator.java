package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;

import java.util.List;

import static com.lamfire.jmongo.query.FilterOperator.NOT_IN;
import static com.lamfire.jmongo.query.validation.CollectionTypeValidator.typeIsIterableOrArrayOrMap;
import static java.lang.String.format;


public final class NotInOperationValidator extends OperationValidator {
    private static final NotInOperationValidator INSTANCE = new NotInOperationValidator();

    private NotInOperationValidator() {
    }


    public static NotInOperationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected FilterOperator getOperator() {
        return NOT_IN;
    }

    @Override
    protected void validate(final MappedField mappedField, final Object value, final List<ValidationFailure> validationFailures) {
        if (value == null) {
            validationFailures.add(new ValidationFailure(format("For a $nin operation, value cannot be null.")));
        } else if (!typeIsIterableOrArrayOrMap(value.getClass())) {
            validationFailures.add(new ValidationFailure(format("For a $nin operation, value '%s' should be a List or array. "
                                                                + "Instead it was a: %s",
                                                                value, value.getClass()
                                                               )));
        }
    }
}
