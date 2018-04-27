


package com.lamfire.jmongo.query;



public class QueryException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public QueryException(final String message) {
        super(message);
    }


    public QueryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
