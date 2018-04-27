package com.lamfire.jmongo.utils;

import static java.lang.String.format;


public final class Assert {
    private Assert() {
    }


    public static void raiseError(final String error) {
        throw new AssertionFailedException(error);
    }


    public static void parametersNotNull(final String names, final Object... objects) {
        String msgPrefix = "At least one of the parameters";

        if (objects != null) {
            if (objects.length == 1) {
                msgPrefix = "Parameter";
            }

            for (final Object object : objects) {
                if (object == null) {
                    raiseError(String.format("%s '%s' is null.", msgPrefix, names));
                }
            }
        }
    }


    public static void parameterNotNull(final String name, final Object reference) {
        if (reference == null) {
            raiseError(format("Parameter '%s' is not expected to be null.", name));
        }
    }


    public static void parameterNotEmpty(final String name, final Iterable obj) {
        if (!obj.iterator().hasNext()) {
            raiseError(format("Parameter '%s' from type '%s' is expected to NOT be empty", name, obj.getClass().getName()));
        }
    }


    public static void parameterNotEmpty(final String name, final String value) {
        if (value != null && value.length() == 0) {
            raiseError(format("Parameter '%s' is expected to NOT be empty.", name));
        }
    }


    public static class AssertionFailedException extends RuntimeException {

        private static final long serialVersionUID = 435272532743543854L;


        public AssertionFailedException(final String message) {
            super(message);
        }


        public AssertionFailedException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
