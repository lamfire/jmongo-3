package com.lamfire.jmongo.mapping.lazy.proxy;


import java.util.ConcurrentModificationException;



public class LazyReferenceFetchingException extends ConcurrentModificationException {
    private static final long serialVersionUID = 1L;


    public LazyReferenceFetchingException(final String msg) {
        super(msg);
    }
}
