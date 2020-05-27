package com.lamfire.jmongo.dao;

import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.QueryResults;
import com.lamfire.jmongo.query.UpdateOperations;
import com.lamfire.jmongo.query.UpdateResults;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DAO<T, K> {

	Query<T> createQuery();

	UpdateOperations<T> createUpdateOperations();

	Class<T> getEntityClass();

	Key<T> save(T entity);

    Key<T> insert(T entity);

	Key<T> save(T entity, WriteConcern wc);

	UpdateResults update(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation);

	UpdateResults update(Query<T> q, UpdateOperations<T> ops);

	UpdateResults update(K k, UpdateOperations<T> ops);
	
	UpdateResults  update(K k, String fieldName, Object value);
	
	UpdateResults  update(K k, Map<String, Object> fieldAndValMap);

	WriteResult delete(T entity);

	WriteResult delete(T entity, WriteConcern wc);

	WriteResult deleteById(K id);

	WriteResult deleteByQuery(Query<T> q);

	T get(K id);

	T get(K id, String... includeFields);

	List<K> findIds(String key, Object value);

	List<K> findIds();

	List<K> findIds(Query<T> q);

	boolean exists(String key, Object value);

	boolean exists(Query<T> q);

	boolean exists(K id);

	long count();

	long count(String key, Object value);

	long count(Query<T> q);

	T findOne(String key, Object value);

	T findOne(Query<T> q);

	QueryResults<T> find();

	QueryResults<T> find(Query<T> q);

	void ensureIndexes();

	DBCollection getCollection();

	Datastore getDatastore();

    DBObject toDBObject(T entity);
	
	List<T> gets(Iterable<K> ids);

	T incrementAndGet(K id, String fieldName);

	T decrementAndGet(K id, String fieldName);

	void increment(K id, String fieldName);

	void decrement(K id, String fieldName);
	
	T incrementAndGet(K id, String fieldName, Number val);
	
	T incrementAndGet(K id, String fieldName, String... includeFields);

	T decrementAndGet(K id, String fieldName, String... includeFields) ;

	void increment(K id, String fieldName, Number val);

	List<Object> distinct(String fieldName);

	List<Object> distinct(String fieldName, Query<T> q);

	UpdateResults setFieldValue(K id, String fieldName, Object value);

	UpdateResults addFieldValue(K id, String fieldName, Object value);

	UpdateResults addFieldValueToSet(K id,String fieldName,Object value);

	UpdateResults addFieldValueToSet(K id,String fieldName,Object value,boolean createIfMissing);

	UpdateResults removeField(K id, String fieldName);

	UpdateResults removeFieldValue(K id, String fieldName, Object value);

	UpdateResults removeFieldValues(K id, String fieldName, Object... values);

	UpdateResults removeFieldValues(K id, String fieldName, Collection<?> values);

	UpdateResults removeFirstFieldValue(K id, String fieldName);

	UpdateResults removeLastFieldValue(K id, String fieldName);

	UpdateResults setFieldValue(K id, String fieldName, Object value, boolean createIfMissing);

	UpdateResults addFieldValue(K id, String fieldName, Object value, boolean createIfMissing);

    Object getFieldValue(K id, String fieldName);

	Object getFieldValueSlice(K id,String fieldName,int slice);

    Map<String,Object> getAsMap(K id);

    Map<String,Object> getAsMap(K id, String... fields);

	Key<T> save(Map<String, Object> map);

    Key<T> save(K id, Map<String, Object> map);

	Key<T> findOneId();
	
	Key<T> findOneId(final String key, final Object value);

	Key<T> findOneId(final Query<T> query);

	UpdateResults updateFirst(final Query<T> query, final UpdateOperations<T> ops);

	WriteResult clear();

}