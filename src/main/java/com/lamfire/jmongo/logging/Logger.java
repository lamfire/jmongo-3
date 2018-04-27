package com.lamfire.jmongo.logging;

public abstract class Logger {

	public abstract void debug(String msg);
    public abstract void debug(String msg, Throwable cause);
    public abstract void debug(Throwable cause);
    
    public abstract void error(String msg) ;
    public abstract void error(String msg, Throwable cause);
    public abstract void error(Throwable cause);

    public abstract void info(String msg);
    public abstract void info(String msg, Throwable cause);
    public abstract void info(Throwable cause);
    
    public abstract void warn(String msg);
    public abstract void warn(String msg, Throwable cause);
    public abstract void warn(Throwable cause);

    public abstract void warning(String msg);
    public abstract void warning(String msg, Throwable cause);
    public abstract void warning(Throwable cause);

    public abstract void trace(String msg);
    public abstract void trace(String msg, Throwable cause);
    public abstract void trace(Throwable cause);
    
    public abstract boolean isDebugEnabled();
    public abstract boolean isErrorEnabled();
    public abstract boolean isInfoEnabled();
    public abstract boolean isWarnEnabled();
    public abstract boolean isTraceEnabled();

    
    
    public static final Logger getLogger(String name){
    	return LoggerFactory.getLogger(name);
    }
    
    
	public static final Logger getLogger(Class<?> cls){
    	return LoggerFactory.getLogger(cls);
    }
}
