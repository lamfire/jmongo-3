package com.lamfire.jmongo;


import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class JMongoZoneOptions {
    protected final List<ServerAddress> seeds = new ArrayList<ServerAddress>();
    protected final MongoClientOptions.Builder optsBuilder = new MongoClientOptions.Builder();
    protected String zone;
    private boolean auth = false;
    private String user;
    private String password;

    public JMongoZoneOptions(String zone){
        this.zone = zone;
        optsBuilder.socketKeepAlive(true);
        optsBuilder.connectionsPerHost(16);
        optsBuilder.threadsAllowedToBlockForConnectionMultiplier(10);
    }

    public void addHost(String host,int port) throws UnknownHostException {
        seeds.add(new ServerAddress(host, port));
    }

    public void addHost(String connectString) throws UnknownHostException{
        String [] val = connectString.split(":");
        addHost(val[0], Integer.parseInt(val[1]));
    }

    public void addHosts(String[] connects) throws UnknownHostException{
        for(String s : connects){
            addHost(s);
        }
    }

    public void addHosts(String seeds) throws UnknownHostException{
        String [] servers = seeds.split(",");
        addHosts(servers);
    }

    public MongoClientOptions getMongoClientOptions(){
        return optsBuilder.build();
    }

    public void setSocketKeepAlive(boolean socketKeepAlive){
        optsBuilder.socketKeepAlive(socketKeepAlive);
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int size){
        optsBuilder.threadsAllowedToBlockForConnectionMultiplier(size);
    }

    public void setConnectionsPerHost(int connectionsPerHost){
        optsBuilder.connectionsPerHost(connectionsPerHost);
    }

    public void setConnectTimeout(int connectTimeout){
        optsBuilder.connectTimeout(connectTimeout);
    }

    public void setMaxWaitTime(int maxWaitTime){
        optsBuilder.maxWaitTime( maxWaitTime);
    }

    public void setSocketTimeout(int socketTimeout){
        optsBuilder.socketTimeout(socketTimeout);
    }

    public List<ServerAddress> getSeeds() {
        return seeds;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setMinConnectionsPerHost(int minConnectionsPerHost) {
        optsBuilder.minConnectionsPerHost(minConnectionsPerHost);
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        optsBuilder.connectionsPerHost(maxConnectionsPerHost);
    }

    public void setServerSelectionTimeout(int serverSelectionTimeout) {
        optsBuilder.serverSelectionTimeout(serverSelectionTimeout);
    }

    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        optsBuilder.maxConnectionIdleTime(maxConnectionIdleTime);
    }

    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        optsBuilder.maxConnectionLifeTime(maxConnectionLifeTime);
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
