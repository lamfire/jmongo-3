package com.lamfire.jmongo;

import com.lamfire.jmongo.logging.Logger;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JMongoZoneRegistry {
    private static final Logger LOGGER = Logger.getLogger("JMongoZoneRegistry");
    private static final JMongoZoneRegistry instance = new JMongoZoneRegistry();

    public static JMongoZoneRegistry getInstance(){
        return instance;
    }

    private Map<String, MongoClient> pool = new HashMap<String, MongoClient>();

    private JMongoZoneRegistry(){

    }

    public synchronized void register(JMongoZoneOptions opts){
        if(opts == null){
            return;
        }
        String zone = opts.getZone();
        MongoClient mongo = pool.get(zone);
        if(mongo != null){
            LOGGER.info("The zone was exists,closing - " + zone);
            mongo.close();
        }

        LOGGER.info("[REGISTER] {" + zone  +"} : " + opts.getConnectionUri());
        MongoClientURI uri = new MongoClientURI(opts.getConnectionUri(),opts.getBuilder());
        mongo = new MongoClient(uri);
        pool.put(zone, mongo);
    }

    public MongoClient getMongoClient(String zone){
        MongoClient client =  pool.get(zone);
        return client;
    }

    public boolean exists(String zone){
        return pool.containsKey(zone);
    }
}
