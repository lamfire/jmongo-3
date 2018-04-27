package com.lamfire.jmongo.query.validation;

import java.util.List;

import static java.lang.String.format;


public final class IntegerTypeValidator extends TypeValidator {
    private static final IntegerTypeValidator INSTANCE = new IntegerTypeValidator();

    private IntegerTypeValidator() {
    }


    public static IntegerTypeValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean appliesTo(final Class<?> type) {
        return type == int.class || type == Integer.class;
    }

    @Override
    protected void validate(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures) {
        if (Integer.class != value.getClass()) {
            validationFailures.add(new ValidationFailure(format("When type is one of the integer types the value should be an Integer.  "
                                                                + "Type was %s and value '%s' was a %s", type, value, value.getClass())));
        }
    }
}
