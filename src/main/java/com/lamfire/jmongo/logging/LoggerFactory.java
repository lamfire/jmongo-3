package com.lamfire.jmongo.logging;


public final class LoggerFactory {

    public static Logger getLogger(String name){
        try {
            return new Log4jLogger(name);
        } catch (Throwable e) {
        }
        try {
            return new Slf4jLogger(name);
        } catch (Throwable e) {
        }
        return new JdkLogger(name);
    }

    public static Logger getLogger(Class<?> claxx){
        return getLogger(claxx.getName());
    }

    public static Logger get(Class<?> claxx){
        return getLogger(claxx.getName());
    }

}
