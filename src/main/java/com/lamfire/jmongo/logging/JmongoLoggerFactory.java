package com.lamfire.jmongo.logging;


public final class JmongoLoggerFactory {

    public static Logger get(final Class<?> c) {
        return LoggerFactory.getLogger(c);
    }

}
