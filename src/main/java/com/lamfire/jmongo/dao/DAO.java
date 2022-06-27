package com.lamfire.jmongo.dao;

import com.lamfire.jmongo.DataStore;
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

	<T> Iterable<Key<T>> save(final Iterable<T>  entities);

	<T> Iterable<Key<T>> save(final T ...  entities);

    Key<T> insert(T entity);

	<T> Iterable<Key<T>> insert(Iterable<T> entities);

	Key<T> save(T entity, WriteConcern wc);

	UpdateResults  update(K k, Map<String, Object> fieldAndValMap);

	UpdateResults update(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal);

	UpdateResults update(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation);

	UpdateResults update(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing);

	UpdateResults update(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMissing);

	UpdateResults update(K k, UpdateOperations<T> ops);
	
	UpdateResults  update(K k, String fieldName, Object value);

	UpdateResults update(K k, UpdateOperations<T> ops,boolean createIfMissing);

	UpdateResults update(K k, String fieldName, Object value,boolean createIfMissing);

	UpdateResults update(Query<T> q, UpdateOperations<T> ops);

	UpdateResults update(Query<T> query, UpdateOperations<T> ops,boolean createIfMissing);

	UpdateResults update(Query<T> query, UpdateOperations<T> ops,boolean createIfMissing,boolean multi);

	UpdateResults update(Query<T> query, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing,boolean multi);

	T updateAndGet(K k, Map<String, Object> fieldAndValMap);

	T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean createIfMissing);

	T updateAndGet(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal);

	T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing);

	T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean oldVersion ,boolean createIfMissing);

	T updateAndGet(Query<T> query, UpdateOperations<T> ops,boolean oldVersion ,boolean createIfMissing);

	T incrementAndGet(K id, String fieldName, Number val);

	T incrementAndGet(K id, Map<String,Number> fieldsAndValues);

	T incrementAndGet(K id, String fieldName, String... includeFields);

	T incrementAndGet(K id, String incField,Number incVal,String whereField,Object whereFieldVal);

	T incrementAndGet(K id, String fieldName, Number val,String... includeFields);

	T incrementAndGet(K id, String fieldName);

    T incrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal);

	T incrementAndGet(K id, String fieldName, Number val,boolean fieldValidation,boolean createIfMiss);

	T incrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean createIfMiss);

	T incrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss);

    T incrementAndGet(Query<T> q, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss);

	T decrementAndGet(K id, String fieldName);

	UpdateResults increment(K id, String fieldName);

	UpdateResults increment(K id, String fieldName, Number val);

	UpdateResults increment(K id, String fieldName,boolean createIfMiss);

	UpdateResults increment(K id, String fieldName, Number val,String whereField,Object whereFieldVal);

	UpdateResults increment(K id, String fieldName, Number val,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);

	UpdateResults increment(K id, String fieldName, Number val,boolean fieldValidation,boolean createIfMiss);

	UpdateResults increment(K id, String fieldName, Number val,boolean createIfMiss);

	UpdateResults increment(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean createIfMiss);

	UpdateResults increment(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal);

	UpdateResults increment(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);

	UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields);

	UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal);

	UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean createIfMiss);

	UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss);

	UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);



	UpdateResults decrement(K id, String fieldName);

	UpdateResults decrement(K id, String fieldName, Number val);

	UpdateResults decrement(K id, String fieldName,boolean createIfMiss);

	UpdateResults decrement(K id, String fieldName, Number val,boolean createIfMiss);

	UpdateResults decrement(K id, String fieldName,Number val,String whereField,Object whereFieldVal);

	UpdateResults decrement(K id, String fieldName,Number val,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);

	UpdateResults decrement(K id, String fieldName,Number val,boolean fieldValidation,boolean createIfMiss);

	UpdateResults decrement(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal);

	UpdateResults decrement(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);

	UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields);

	UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal);

	UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean createIfMiss);

	UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss);

	UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss);

	T decrementAndGet(K id, String fieldName, Number val);

    T decrementAndGet(K id, String fieldName, Number val,String whereField,Object whereFieldVal);

	T decrementAndGet(K id, String fieldName, String... includeFields) ;

	T decrementAndGet(K id, String fieldName, Number val,String... includeFields);

    T decrementAndGet(K id, Map<String,Number> fieldsAndValues);

    T decrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean createIfMiss);

    T decrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal);

    T decrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss);

    T decrementAndGet(Query<T> q, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss);

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

	DataStore getDataStore();

	DBObject toDBObject(T entity);

	List<T> gets(Iterable<K> ids);

	WriteResult clear();

}