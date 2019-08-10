package com.lamfire.jmongo;

public class JMongoZoneOptions {
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
}
