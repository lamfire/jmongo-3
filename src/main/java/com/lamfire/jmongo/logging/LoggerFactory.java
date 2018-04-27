package com.lamfire.jmongo.logging;

public class LoggerFactory {
	static final String Log4J_LOGGER = "org.apache.log4j.Logger";
	static final String Slf4J_LOGGER = "org.slf4j.impl.StaticLoggerBinder";
	private static boolean hasSlf4j = false;
	private static boolean hasLog4j = false;
	static{
		try {
            Class.forName(Log4J_LOGGER);
            hasLog4j = true;
        } catch (Exception e) {
        }
        
        if(!hasLog4j){
			try {
	            Class.forName(Slf4J_LOGGER);
	            hasSlf4j = true;
	        } catch (Exception e) {
	        }
        }
	}

    public static Logger getLogger(String name){
    	if(hasLog4j){
            return new Log4jLogger(name);
        }
        if(hasSlf4j){
            return new Slf4jLogger(name);
        }
        return new JdkLogger(name);
    }
    
    public static Logger getLogger(Class<?> claxx){
        return getLogger(claxx.getName());
    }
}
