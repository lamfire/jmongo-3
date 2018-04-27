


package com.lamfire.jmongo.query;



public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public ValidationException(final String message) {
        super(message);
    }


    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
