package com.lamfire.jmongo.logging;

public class Slf4jLogger extends Logger{

    private org.slf4j.Logger logger ;
    
    public Slf4jLogger(Class<?> cls){
        this.logger = org.slf4j.LoggerFactory.getLogger(cls);
    }
    
    public Slf4jLogger(String name){
        this.logger = org.slf4j.LoggerFactory.getLogger(name);
    }
    
    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable cause) {
        logger.debug(msg,cause);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String msg, Throwable cause) {
        logger.error(msg,cause);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String msg, Throwable cause) {
        logger.info(msg,cause);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable cause) {
        logger.warn(msg,cause);
    }

	@Override
	public void debug(Throwable cause) {
		logger.debug(cause.getMessage(), cause);
	}

	@Override
	public void error(Throwable cause) {
		logger.error(cause.getMessage(), cause);
		
	}

	@Override
	public void info(Throwable cause) {
		logger.info(cause.getMessage(), cause);
	}

	@Override
	public void warn(Throwable cause) {
		logger.warn(cause.getMessage(), cause);
		
	}

    @Override
    public void warning(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warning(String msg, Throwable cause) {
        logger.warn(msg,cause);
    }

    @Override
    public void warning(Throwable cause) {
        logger.warn(cause.getLocalizedMessage(),cause);
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String msg, Throwable cause) {
        logger.trace(msg,cause);
    }

    @Override
    public void trace(Throwable cause) {
        logger.trace(cause.getMessage(),cause);
    }

}
