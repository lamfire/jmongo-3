package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.annotations.Entity;

import java.util.List;

import static java.lang.String.format;


public final class EntityAnnotatedValueValidator extends TypeValidator {
    private static final EntityAnnotatedValueValidator INSTANCE = new EntityAnnotatedValueValidator();

    private EntityAnnotatedValueValidator() {
    }


    public static EntityAnnotatedValueValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean appliesTo(final Class<?> type) {
        return Key.class.equals(type);
    }

    @Override
    protected void validate(final Class<?> type, final Object value, final List<ValidationFailure> validationFailures) {
        if (value.getClass().getAnnotation(Entity.class) == null) {
            validationFailures.add(new ValidationFailure(format("When type is a Key the value should be an annotated entity. "
                                                                + "Value '%s' was a %s", value, value.getClass())));

        }
    }
}
