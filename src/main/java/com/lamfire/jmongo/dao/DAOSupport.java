package com.lamfire.jmongo.dao;


public class DAOSupport <T,K> extends JMongoDAO {

    public DAOSupport(String zoneName, String dbName, String colName, Class entityClass) {
        super(zoneName, dbName, colName, entityClass);
    }
}
