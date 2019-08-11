package com.lamfire.jmongo;


import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.mongodb.MongoClientURI;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

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
		for(Entry<Object,Object> e : properties.entrySet()){
			String key = e.getKey().toString();
			String val = e.getValue().toString();
			LOGGER.info("[FOUND] : " + key +" = " + val );
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
	
	public JMongoZoneOptions getJMongoZoneOptions(String zone){
		JMongoZoneOptions opts = optsMap.get(zone);
		if(opts == null){
			opts = buildMongoOpts(zone);
			optsMap.put(zone, opts);
		}
		return opts;
	}

	public Collection<JMongoZoneOptions> getAllJMongoZoneOptions(){
		return getAllJMongoZoneOptionsAsMap().values();
	}
	
	public Map<String, JMongoZoneOptions> getAllJMongoZoneOptionsAsMap(){
		return Collections.unmodifiableMap(this.optsMap);
	}

	public boolean isMongoUri(final String addr){
		if(addr != null && addr.toLowerCase().startsWith("mongodb://")){
			return true;
		}
		return false;
	}
	
	private JMongoZoneOptions buildMongoOpts(String zone){
		Map<String, String> conf = confMap.get(zone);
		if(conf == null){
			return null;
		}
		LOGGER.info("[BUILDING] {"+zone+"'} : "+conf.toString());

		String connectionUri = conf.get("connectionUri");
		
		if(connectionUri == null || "".equals(connectionUri)){
			throw new RuntimeException("the property '"+zone+".connectionUri' was required,please check config file 'jmongo.properties'.ex : 'mongodb://root:123456@192.168.56.11:27017/admin'");
		}

		return new JMongoZoneOptions(zone,connectionUri);
	}
}
