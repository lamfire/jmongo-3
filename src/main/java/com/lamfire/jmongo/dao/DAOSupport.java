package com.lamfire.jmongo.dao;


import com.lamfire.jmongo.Mapping;
import com.mongodb.MongoClient;

public class DAOSupport <T,K> extends JMongoDAO <T,K>{

    public DAOSupport(String zoneName, String dbName, String colName, Class entityClass) {
        super(zoneName, dbName, colName, entityClass);
    }

    public DAOSupport(final MongoClient client, Mapping mapping, final String dbName, String colName , final Class<T> entityClass){
        super(client,mapping, dbName, colName, entityClass);
    }
}
