package com.lamfire.jmongo;

import com.lamfire.jmongo.logging.Logger;
import com.mongodb.Mongo;

import java.util.HashMap;
import java.util.Map;

public class JMongoIndexesMgr {
    private static final Logger LOGGER = Logger.getLogger(JMongoIndexesMgr.class);
    private final Map<String,Class<?>> indexes = new HashMap<String, Class<?>>();

    private static final JMongoIndexesMgr instance = new JMongoIndexesMgr();

    public static JMongoIndexesMgr getInstance(){
        return instance;
    }

    private String getKey(Mongo mongo, Datastore ds, String kind){
        String key = mongo.getAddress() +"/" + ds.getDB().getName()+ "/" + kind;
        return key;
    }

    public boolean isEnsureIndexes(Mongo mongo, Datastore ds, String kind){
        String key = getKey(mongo,ds,kind);
        return indexes.containsKey(key);
    }

    public synchronized void ensureIndexes(Mongo mongo, JMongoDataStore ds, String colName, Class<?> entityClazz){
        String key = getKey(mongo,ds,colName);
        if(indexes.containsKey(key)){
            return;
        }
        LOGGER.info("[EnsureIndexes] : " + key +" -> " + entityClazz.getName());
        ds.ensureIndexes(colName,entityClazz);
        indexes.put(key,entityClazz);
    }


}
