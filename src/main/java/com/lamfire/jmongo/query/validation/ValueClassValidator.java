package com.lamfire.jmongo.query.validation;


final class ValueClassValidator implements Validator {
    private ValueClassValidator() {
    }


    static boolean valueIsClassOrSubclassOf(final Object value,
                                            final Class<?> requiredClass) {
        return (requiredClass.isAssignableFrom(value.getClass()));
    }

}
