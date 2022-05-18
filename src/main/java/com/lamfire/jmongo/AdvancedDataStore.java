package com.lamfire.jmongo;

import com.lamfire.jmongo.aggregation.AggregationPipeline;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.UpdateOperations;
import com.mongodb.*;

public interface AdvancedDataStore extends DataStore {


    DBDecoderFactory getDecoderFact();


    void setDecoderFact(DBDecoderFactory fact);


    AggregationPipeline createAggregation(String collection, Class<?> clazz);


    <T> Query<T> createQuery(String collection, Class<T> clazz);


    <T> Query<T> createQuery(Class<T> clazz, DBObject q);

    <T> Query<T> createQuery(String collection, Class<T> clazz, DBObject q);


    <T, V> DBRef createRef(Class<T> clazz, V id);


    <T> DBRef createRef(T entity);


    <T> UpdateOperations<T> createUpdateOperations(Class<T> type, DBObject ops);


    <T, V> WriteResult delete(String kind, Class<T> clazz, V id);


    <T, V> WriteResult delete(String kind, Class<T> clazz, V id, DeleteOptions options);


    @Deprecated
    <T, V> WriteResult delete(String kind, Class<T> clazz, V id, WriteConcern wc);


    @Deprecated
    <T> void ensureIndex(String collection, Class<T> clazz, String fields);


    @Deprecated
    <T> void ensureIndex(String collection, Class<T> clazz, String name,
                         String fields, boolean unique, boolean dropDupsOnCreate);


    <T> void ensureIndexes(String collection, Class<T> clazz);


    <T> void ensureIndexes(String collection, Class<T> clazz, boolean background);


    Key<?> exists(Object keyOrEntity, ReadPreference readPreference);


    <T> Query<T> find(String collection, Class<T> clazz);


    <T, V> Query<T> find(String collection, Class<T> clazz, String property, V value, int offset, int size);


    <T> T get(Class<T> clazz, DBRef ref);


    <T, V> T get(String collection, Class<T> clazz, V id);


    long getCount(String collection);


    <T> Key<T> insert(T entity);


    @Deprecated
    <T> Key<T> insert(T entity, WriteConcern wc);


    <T> Key<T> insert(T entity, InsertOptions options);


    <T> Key<T> insert(String collection, T entity);


    <T> Key<T> insert(String collection, T entity, InsertOptions options);


    @Deprecated
    <T> Iterable<Key<T>> insert(T... entities);


    <T> Iterable<Key<T>> insert(Iterable<T> entities);


    @Deprecated
    <T> Iterable<Key<T>> insert(Iterable<T> entities, WriteConcern wc);


    <T> Iterable<Key<T>> insert(Iterable<T> entities, InsertOptions options);


    <T> Iterable<Key<T>> insert(String collection, Iterable<T> entities);


    @Deprecated
    <T> Iterable<Key<T>> insert(String collection, Iterable<T> entities, WriteConcern wc);


    <T> Iterable<Key<T>> insert(String collection, Iterable<T> entities, InsertOptions options);


    <T> Query<T> queryByExample(String collection, T example);


    <T> Key<T> save(String collection, T entity);


    @Deprecated
    <T> Key<T> save(String collection, T entity, WriteConcern wc);


    <T> Key<T> save(String collection, T entity, InsertOptions options);

}
