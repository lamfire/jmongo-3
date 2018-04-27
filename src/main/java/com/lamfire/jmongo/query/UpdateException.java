


package com.lamfire.jmongo.query;



public class UpdateException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public UpdateException(final String message) {
        super(message);
    }


    public UpdateException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
