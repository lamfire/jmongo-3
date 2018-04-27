package com.lamfire.jmongo.odao;


import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.QueryResults;
import com.lamfire.jmongo.query.UpdateOperations;
import com.lamfire.jmongo.query.UpdateResults;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import java.util.List;



interface DAO<T, K> {

    long count();


    long count(String key, Object value);


    long count(Query<T> query);


    Query<T> createQuery();


    UpdateOperations<T> createUpdateOperations();


    WriteResult delete(T entity);


    WriteResult delete(T entity, WriteConcern wc);


    WriteResult deleteById(K id);


    WriteResult deleteByQuery(Query<T> query);


    void ensureIndexes();


    boolean exists(String key, Object value);


    boolean exists(Query<T> query);


    QueryResults<T> find();


    QueryResults<T> find(Query<T> query);


    List<K> findIds();


    List<K> findIds(String key, Object value);


    List<K> findIds(Query<T> query);


    T findOne(String key, Object value);


    T findOne(Query<T> query);


    Key<T> findOneId();


    Key<T> findOneId(String key, Object value);


    Key<T> findOneId(Query<T> query);


    T get(K id);


    DBCollection getCollection();


    Datastore getDatastore();


    Class<T> getEntityClass();


    Key<T> save(T entity);


    Key<T> save(T entity, WriteConcern wc);


    UpdateResults update(Query<T> query, UpdateOperations<T> ops);


    UpdateResults updateFirst(Query<T> query, UpdateOperations<T> ops);
}
