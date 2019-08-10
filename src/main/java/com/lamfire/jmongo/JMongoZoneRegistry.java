package com.lamfire.jmongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JMongoZoneRegistry {

    private static final JMongoZoneRegistry instance = new JMongoZoneRegistry();

    public static JMongoZoneRegistry getInstance(){
        return instance;
    }

    private Map<String, MongoClient> pool = new HashMap<String, MongoClient>();

    private JMongoZoneRegistry(){

    }

    public synchronized void register(JMongoZoneOptions opts){
        if(opts == null){
            throw new RuntimeException("the parameter 'MongoOpts' cannot be null.");
        }
        String zone = opts.getZone();
        MongoClient mongo = pool.get(zone);
        if(mongo != null){
            throw new RuntimeException("the zone was exists : " + zone);
        }

        MongoClientURI uri = new MongoClientURI(opts.getConnectionUri());

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
