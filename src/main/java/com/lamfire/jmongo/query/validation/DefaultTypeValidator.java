package com.lamfire.jmongo.query.validation;

import java.util.List;

import static java.lang.String.format;


public final class DefaultTypeValidator extends TypeValidator {
    private static final DefaultTypeValidator INSTANCE = new DefaultTypeValidator();

    private DefaultTypeValidator() {
    }


    public static DefaultTypeValidator getInstance() {
        return INSTANCE;
    }


    @Override
    protected boolean appliesTo(final Class<?> type) {
        return true;
    }

    @Override
    protected void validate(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures) {
        if (!type.isAssignableFrom(value.getClass())
            && !value.getClass().getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
            validationFailures.add(new ValidationFailure(format("Type %s may not be queryable with value '%s' with class %s",
                                                                type.getCanonicalName(),
                                                                value, value.getClass().getCanonicalName())));
        }
    }
}
