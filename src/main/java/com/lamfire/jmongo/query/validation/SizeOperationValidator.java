package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;

import java.util.List;

import static com.lamfire.jmongo.query.FilterOperator.SIZE;
import static com.lamfire.jmongo.query.validation.ValueClassValidator.valueIsClassOrSubclassOf;
import static java.lang.String.format;


public final class SizeOperationValidator extends OperationValidator {
    private static final SizeOperationValidator INSTANCE = new SizeOperationValidator();

    private SizeOperationValidator() {
    }


    public static SizeOperationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected FilterOperator getOperator() {
        return SIZE;
    }

    @Override
    protected void validate(final MappedField mappedField, final Object value,
                            final List<ValidationFailure> validationFailures) {
        if (!valueIsClassOrSubclassOf(value, Number.class)) {
            validationFailures.add(new ValidationFailure(format("For a $size operation, value '%s' should be an integer type.  "
                                                                + "Instead it was a: %s", value, value.getClass())));

        }
        if (!CollectionTypeValidator.typeIsIterableOrArrayOrMap(mappedField.getType())) {
            validationFailures.add(new ValidationFailure(format("For a $size operation, field '%s' should be a List or array.  "
                                                                + "Instead it was a: %s",
                                                                mappedField, mappedField.getType())));
        }
    }
}
