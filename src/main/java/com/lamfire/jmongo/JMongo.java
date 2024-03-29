package com.lamfire.jmongo;


import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.dao.JMongoDAO;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.mongodb.MongoClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JMongo {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMongo.class);
    private static final Map<String, Mapping> mappings = new HashMap<String, Mapping>();
    private static final Map<String, DAO> daos = new HashMap<String, DAO>();


    static{
        registerZoneByConfiguration();
    }

    public synchronized static <T,K> DAO<T, K> getDAO(final String zone, final String dbName, final String colName , final Class<T> entityClass){
        String key = String.format("%s/%s/%s/%s",zone,dbName,colName,entityClass.getName());
        DAO dao = daos.get(key);
        if(dao != null){
            return dao;
        }

        LOGGER.debug("[Create Cached DAO For] : " + key );
        dao =  new JMongoDAO(zone,dbName,colName,entityClass);
        daos.put(key,dao);
        return dao;
    }

    public static synchronized void registerJMongoZoneOptions(JMongoZoneOptions zoneOptions){
        JMongoZoneRegistry registry = JMongoZoneRegistry.getInstance();
        registry.register(zoneOptions);
    }

    private static void registerZoneByConfiguration(){
        Collection<JMongoZoneOptions> opts = JMongoConfiguration.getInstance().getAllJMongoZoneOptions();
        for(JMongoZoneOptions a : opts) {
            registerJMongoZoneOptions(a);
        }
    }

    public static synchronized MongoClient getMongoClient(String zone){
        JMongoZoneRegistry registry = JMongoZoneRegistry.getInstance();
        return registry.getMongoClient(zone);
    }


    public static synchronized Mapping getMapping(String zone,String dbName){
        String key = String.format("%s:%s",zone,dbName);
        Mapping mapping = mappings.get(key);
        if(mapping != null){
            return mapping;
        }
        mapping = new Mapping();
        mappings.put(key, mapping);
        return mapping;
    }
}
