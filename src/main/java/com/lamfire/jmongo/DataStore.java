package com.lamfire.jmongo;


import com.lamfire.jmongo.aggregation.AggregationPipeline;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.*;
import com.mongodb.*;

import java.util.List;
import java.util.Map;



public interface DataStore {

    AggregationPipeline createAggregation(Class source);


    <T> Query<T> createQuery(Class<T> collection);


    <T> UpdateOperations<T> createUpdateOperations(Class<T> clazz);


    <T, V> WriteResult delete(Class<T> clazz, V id);


    <T, V> WriteResult delete(Class<T> clazz, V id, DeleteOptions options);


    <T, V> WriteResult delete(Class<T> clazz, Iterable<V> ids);


    <T, V> WriteResult delete(Class<T> clazz, Iterable<V> ids, DeleteOptions options);


    <T> WriteResult delete(Query<T> query);


    <T> WriteResult delete(Query<T> query, DeleteOptions options);

    <T> WriteResult delete(Query<T> query, WriteConcern wc);


    <T> WriteResult delete(T entity);


    <T> WriteResult delete(T entity, DeleteOptions options);

    <T> WriteResult delete(T entity, WriteConcern wc);


    void ensureCaps();


    void enableDocumentValidation();

    <T> void ensureIndex(Class<T> clazz, String fields);

    <T> void ensureIndex(Class<T> clazz, String name, String fields, boolean unique, boolean dropDupsOnCreate);


    void ensureIndexes();


    void ensureIndexes(boolean background);


    <T> void ensureIndexes(Class<T> clazz);


    <T> void ensureIndexes(Class<T> clazz, boolean background);


    Key<?> exists(Object keyOrEntity);


    <T> Query<T> find(Class<T> clazz);

    <T> Query<T> find(String collection,Class<T> clazz);


    <T> Query<T> createQuery(final String collection, final Class<T> type);

    <T, V> Query<T> find(Class<T> clazz, String property, V value);

    <T, V> Query<T> find(Class<T> clazz, String property, V value, int offset, int size);


    <T> T findAndDelete(Query<T> query);

    <T> T findAndDelete(Query<T> query, FindAndModifyOptions options);


    <T> T findAndModify(Query<T> query, UpdateOperations<T> operations, FindAndModifyOptions options);


    <T> T findAndModify(Query<T> query, UpdateOperations<T> operations);

    <T> T findAndModify(Query<T> query, UpdateOperations<T> operations, boolean oldVersion);

    <T> T findAndModify(Query<T> query, UpdateOperations<T> operations, boolean oldVersion, boolean createIfMissing);


    <T, V> Query<T> get(Class<T> clazz, Iterable<V> ids);


    <T, V> T get(Class<T> clazz, V id);


    <T> T get(T entity);


    <T> T getByKey(Class<T> clazz, Key<T> key);


    <T> List<T> getByKeys(Class<T> clazz, Iterable<Key<T>> keys);


    <T> List<T> getByKeys(Iterable<Key<T>> keys);


    DBCollection getCollection(Class<?> clazz);


    <T> long getCount(T entity);


    <T> long getCount(Class<T> clazz);

    long getCount(final String kind);


    <T> long getCount(Query<T> query);


    <T> long getCount(Query<T> query, CountOptions options);


    DB getDB();


    WriteConcern getDefaultWriteConcern();


    void setDefaultWriteConcern(WriteConcern wc);


    <T> Key<T> getKey(T entity);


    MongoClient getMongo();


    QueryFactory getQueryFactory();


    void setQueryFactory(QueryFactory queryFactory);


    <T> MapreduceResults<T> mapReduce(MapReduceOptions<T> options);

    <T> MapreduceResults<T> mapReduce(MapreduceType type, Query q, String map, String reduce, String finalize,
                                      Map<String, Object> scopeFields, Class<T> outputType);

    <T> MapreduceResults<T> mapReduce(MapreduceType type, Query q, Class<T> outputType, MapReduceCommand baseCommand);


    <T> Key<T> merge(T entity);


    <T> Key<T> merge(T entity, WriteConcern wc);


    <T> Query<T> queryByExample(T example);


    <T> Iterable<Key<T>> save(Iterable<T> entities);

    <T> Iterable<Key<T>> save(Iterable<T> entities, WriteConcern wc);


    <T> Iterable<Key<T>> save(Iterable<T> entities, InsertOptions options);

    <T> Iterable<Key<T>> save(T... entities);


    <T> Key<T> save(T entity);

    <T> Key<T> save(T entity, WriteConcern wc);


    <T> Key<T> save(T entity, InsertOptions options);


    <T> UpdateResults update(T entity, UpdateOperations<T> operations);


    <T> UpdateResults update(Key<T> key, UpdateOperations<T> operations);


    <T> UpdateResults update(Query<T> query, UpdateOperations<T> operations);


    <T> UpdateResults update(Query<T> query, UpdateOperations<T> operations, UpdateOptions options);

    <T> UpdateResults update(Query<T> query, UpdateOperations<T> operations, boolean createIfMissing);

    <T> UpdateResults update(Query<T> query, UpdateOperations<T> operations, boolean createIfMissing, WriteConcern wc);

    <T> UpdateResults updateFirst(Query<T> query, UpdateOperations<T> operations);

    <T> UpdateResults updateFirst(Query<T> query, UpdateOperations<T> operations, boolean createIfMissing);

    <T> UpdateResults updateFirst(Query<T> query, UpdateOperations<T> operations, boolean createIfMissing, WriteConcern wc);

    <T> UpdateResults updateFirst(Query<T> query, T entity, boolean createIfMissing);

    Mapper getMapper();

    DBDecoderFactory getDecoderFact();

}
