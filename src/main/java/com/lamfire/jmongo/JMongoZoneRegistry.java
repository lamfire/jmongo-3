package com.lamfire.jmongo;

import com.mongodb.MongoClient;

import java.util.HashMap;
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


        mongo =  new MongoClient(opts.seeds,opts.getMongoClientOptions());

        //mongo.setReadPreference(ReadPreference.secondaryPreferred());
        //mongo.setWriteConcern(WriteConcern.NORMAL);
        pool.put(zone, mongo);
    }

    public MongoClient getMongoClient(String zone){
        return pool.get(zone);
    }
}
