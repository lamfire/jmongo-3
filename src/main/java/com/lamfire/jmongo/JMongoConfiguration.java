package com.lamfire.jmongo;


import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.logging.LoggerFactory;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class JMongoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(JMongoConfiguration.class);
	private static final String CONFIG_FILE = "jmongo.properties";
	private Map<String, JMongoZoneOptions> optsMap = new HashMap<String, JMongoZoneOptions>();
	private Map<String, Map<String, String>> confMap = new HashMap<String, Map<String, String>>();

	private static final JMongoConfiguration instance = new JMongoConfiguration();;

	public static final JMongoConfiguration getInstance(){
		return instance;
	}

	private JMongoConfiguration(){
		loadConfigureFile();
	}

    private static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
	
	private void loadConfigureFile(){
        Properties properties = new Properties();
        try{
            InputStream input = JMongoConfiguration.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            properties.load(input);
        }catch (Throwable t){
			LOGGER.error("Cannot open configure file : " + CONFIG_FILE);
            t.printStackTrace();
        }
		//Map<String,String> map = PropertiesUtils.loadAsMap(CONFIG_FILE, Configuration.class);
		for(Entry<Object,Object> e : properties.entrySet()){
			String key = e.getKey().toString();
			String val = e.getValue().toString();
			LOGGER.info("[Configure]:" + key +" = " + val );
			//String[] keys = StringUtils.split(key,".",2);
            String[] keys = key.split("\\.");
			if(keys.length != 2){
				continue;
			}
			String id = keys[0];
			String opt = keys[1];
			setConfigValue(id,opt,val);
		}
		for(String id : confMap.keySet()){
			JMongoZoneOptions opts = buildMongoOpts(id);
			optsMap.put(id, opts);
		}
	}
	
	private void setConfigValue(String id, String optKey, String optVal){
		Map<String, String> map = confMap.get(id);
		if(map == null){
			map = new HashMap<String, String>();
			confMap.put(id, map);
		}
		map.put(optKey, optVal);
	}
	
	public JMongoZoneOptions getJMongoOptions(String zone){
		JMongoZoneOptions opts = optsMap.get(zone);
		if(opts == null){
			opts = buildMongoOpts(zone);
			optsMap.put(zone, opts);
		}
		return opts;
	}
	
	public Map<String, JMongoZoneOptions> getAllMongoOpts(){
		return Collections.unmodifiableMap(this.optsMap);
	}
	
	private JMongoZoneOptions buildMongoOpts(String zone){
		Map<String, String> conf = confMap.get(zone);
		if(conf == null){
			return null;
		}
		LOGGER.info("[BUILDING '"+zone+"']:"+conf.toString());
		JMongoZoneOptions opts = new JMongoZoneOptions(zone);
		String seeds = conf.get("servers");
		
		if(seeds == null){
			throw new RuntimeException("the property '"+zone+".servers' was required,please check config file 'jmongo.properties'.");
		}
		
		try {
			opts.addHosts(seeds);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		String connectionsPerHost = conf.get("connectionsPerHost");
		String connectTimeout=conf.get("connectTimeout");
		String maxWaitTime=conf.get("maxWaitTime");
		String socketTimeout=conf.get("socketTimeout");
		String socketKeepAlive=conf.get("socketKeepAlive");
		String threadsAllowedToBlockForConnectionMultiplier = conf.get("threadsAllowedToBlockForConnectionMultiplier");

		String minConnectionsPerHost = conf.get("minConnectionsPerHost");
		String maxConnectionsPerHost = conf.get("maxConnectionsPerHost");
		String serverSelectionTimeout = conf.get("serverSelectionTimeout");
		String 	maxConnectionIdleTime = conf.get("maxConnectionIdleTime");
		String maxConnectionLifeTime = conf.get("maxConnectionLifeTime");


		String auth = conf.get("auth");
		String 	user = conf.get("user");
		String password = conf.get("password");
		String database = conf.get("database");

		if(!isBlank(connectionsPerHost)){
			opts.setConnectionsPerHost(Integer.parseInt(connectionsPerHost));
		}
		
		if(!isBlank(threadsAllowedToBlockForConnectionMultiplier)){
			opts.setThreadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier));
		}
		
		if(!isBlank(connectTimeout)){
			opts.setConnectTimeout(Integer.parseInt(connectTimeout));
		}

		if(!isBlank(maxWaitTime)){
			opts.setMaxWaitTime(Integer.parseInt(maxWaitTime));
		}
		
		if(!isBlank(socketTimeout)){
			opts.setSocketTimeout(Integer.parseInt(socketTimeout));
		}

		if(!isBlank(socketKeepAlive)){
			opts.setSocketKeepAlive(Boolean.parseBoolean(socketKeepAlive));
		}

		if(!isBlank(minConnectionsPerHost)){
			opts.setMinConnectionsPerHost(Integer.parseInt(minConnectionsPerHost));
		}

		if(!isBlank(maxConnectionsPerHost)){
			opts.setMaxConnectionsPerHost(Integer.parseInt(maxConnectionsPerHost));
		}

		if(!isBlank(serverSelectionTimeout)){
			opts.setServerSelectionTimeout(Integer.parseInt(serverSelectionTimeout));
		}

		if(!isBlank(maxConnectionIdleTime)){
			opts.setMaxConnectionIdleTime(Integer.parseInt(maxConnectionIdleTime));
		}

		if(!isBlank(maxConnectionLifeTime)){
			opts.setMaxConnectionLifeTime(Integer.parseInt(maxConnectionLifeTime));
		}

		if(!isBlank(auth)){
			opts.setAuth(Boolean.valueOf(auth));
		}

		opts.setUser(user);
		opts.setPassword(password);
		opts.setDatabase(database);

		return opts;
	}
}
