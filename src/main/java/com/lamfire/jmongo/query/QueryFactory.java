package com.lamfire.jmongo.query;

import com.lamfire.jmongo.Datastore;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


public interface QueryFactory {


    <T> Query<T> createQuery(Datastore datastore, DBCollection collection, Class<T> type);


    <T> Query<T> createQuery(Datastore datastore, DBCollection collection, Class<T> type, DBObject query);


    <T> Query<T> createQuery(Datastore datastore);
}
