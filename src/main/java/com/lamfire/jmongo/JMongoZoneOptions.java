package com.lamfire.jmongo;

import com.mongodb.MongoClientOptions;

public class JMongoZoneOptions {
    private final MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
    protected String zone;
    private String connectionUri;

    public JMongoZoneOptions(String zone,String connectionUri){
        this.zone = zone;
        this.connectionUri = connectionUri;
    }

    public String getZone() {
        return zone;
    }


    public String getConnectionUri() {
        return connectionUri;
    }

    public MongoClientOptions.Builder getBuilder() {
        return builder;
    }
}
