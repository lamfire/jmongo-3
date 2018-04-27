

package com.lamfire.jmongo.mapping;


public class MappingException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public MappingException(final String message) {
        super(message);
    }


    public MappingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
