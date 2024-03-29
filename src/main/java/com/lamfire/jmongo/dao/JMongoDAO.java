package com.lamfire.jmongo.dao;

import com.lamfire.jmongo.*;
import com.lamfire.jmongo.InsertOptions;
import com.lamfire.jmongo.aggregation.AggregationPipeline;
import com.lamfire.jmongo.annotations.Id;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.*;
import com.mongodb.*;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.*;


public class JMongoDAO<T, K> implements DAO<T, K> {
    private String dbName;
    private String colName;
    protected Class<T> entityClazz;
    protected String IdFieldName;

    private MongoClient mongoClient;
    private Mapping mapping;
    private JMongoDataStore ds;

    public JMongoDAO(final MongoClient client,Mapping mapping, final String dbName,String colName ,final Class<T> entityClass) {
        this.dbName = dbName;
        this.colName = colName;
        this.entityClazz = entityClass;

        this.mongoClient = client;
        this.mapping = mapping;

        init();
    }

    public JMongoDAO(final String zone, final String dbName,String colName ,final Class<T> entityClass) {
        this(JMongo.getMongoClient(zone),JMongo.getMapping(zone,dbName),dbName,colName,entityClass);
    }

    protected void init() {
        ds = new JMongoDataStore(mapping,mongoClient, dbName);
        Mapper mapper = mapping.getMapper();
        mapper.addMappedClass(entityClazz);
        JMongoIndexesMgr.getInstance().ensureIndexes(this.mongoClient,ds,colName,entityClazz);
    }

    @Override
    public long count() {
        return ds.getCount(colName);
    }

    @Override
    public long count(final String key, final Object value) {
        return count(ds.find(colName,this.entityClazz).filter(key, value));
    }

    @Override
    public long count(final Query<T> query) {
        return ds.getCount(query);
    }

    @Override
    public Query<T> createQuery() {
        return ds.find(colName,this.entityClazz);
    }

    @Override
    public UpdateOperations<T> createUpdateOperations() {
        return ds.createUpdateOperations(entityClazz);
    }

    @Override
    public WriteResult delete(final T entity) {
        return ds.delete(colName,entity);
    }

    @Override
    public WriteResult delete(final T entity, final WriteConcern wc) {
        return ds.delete(colName,entity, wc);
    }

    @Override
    public WriteResult deleteById(final K id) {
        return ds.delete(colName,entityClazz, id);
    }

    @Override
    public WriteResult deleteByQuery(final Query<T> query) {
        return ds.delete(query);
    }

    @Override
    public void ensureIndexes() {
        ds.ensureIndexes(colName,entityClazz);
    }

    @Override
    public boolean exists(final String key, final Object value) {
        return exists(ds.find(colName,entityClazz).filter(key, value));
    }

    @Override
    public boolean exists(final Query<T> query) {
        return query.get(new FindOptions().limit(1)) != null;
    }

    @Override
    public boolean exists(K id) {
        return get(id) != null;
    }

    @Override
    public QueryResults<T> find() {
        return createQuery();
    }

    @Override
    public QueryResults<T> find(final Query<T> query) {
        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<K> findIds() {
        return (List<K>) keysToIds(ds.find(colName,entityClazz).asKeyList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<K> findIds(final String key, final Object value) {
        return (List<K>) keysToIds(ds.find(colName,entityClazz).filter(key, value).asKeyList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<K> findIds(final Query<T> query) {
        return (List<K>) keysToIds(query.asKeyList());
    }

    @Override
    public T findOne(final String key, final Object value) {
        return ds.find(colName,entityClazz).filter(key, value).get();
    }


    @Override
    public T findOne(final Query<T> query) {
        return query.get();
    }

    @Override
    public Key<T> findOneId() {
        return findOneId(ds.find(colName,entityClazz));
    }

    @Override
    public Key<T> findOneId(final String key, final Object value) {
        return findOneId(ds.find(colName,entityClazz).filter(key, value));
    }

    @Override
    public Key<T> findOneId(final Query<T> query) {
        Iterator<Key<T>> keys = query.fetchKeys().iterator();
        return keys.hasNext() ? keys.next() : null;
    }

    @Override
    public T get(final K id) {
        return ds.get(colName,entityClazz, id);
    }

    public T getByPrimaryOnly(final K id) {
        Query<T> q = createQuery();
        List<T> list = q.queryPrimaryOnly().field(Mapper.ID_KEY).equal(id).asList();
        if(list==null || list.isEmpty())return null;
        return list.get(0);
    }

    @Override
    public T get(K id, String... fields) {
        Query<T> q = createQuery();
        q.includeFieldsOnly(fields);
        q.field(Mapper.ID_KEY).equal(id);
        List<T> list = q.asList();
        if(list==null || list.isEmpty())return null;
        return list.get(0);
    }

    @Override
    public DBCollection getCollection() {
        return ds.getCollection(colName);
    }


    @Override
    public JMongoDataStore getDataStore() {
        return ds;
    }

    @Override
    public DBObject toDBObject(T entity) {
        return mapping.toDBObject(entity);
    }

    @Override
    public List<T> gets(Iterable<K> ids) {
        return this.createQuery().field("_id").in(ids).asList();
    }

    @Override
    public T incrementAndGet(K id, String fieldName) {
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName);
        return this.ds.findAndModify(q, uOps);
    }

    @Override
    public T decrementAndGet(K id, String fieldName) {
        Query<T> q = this.ds.find(colName,entityClazz, "_id", id);
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName);
        return this.ds.findAndModify(q, uOps);
    }

    public T decrementAndGet(K id, String fieldName,Number val) {
        Query<T> q = this.ds.find(colName,entityClazz, "_id", id);
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName,val);
        return this.ds.findAndModify(q, uOps);
    }

    @Override
    public UpdateResults increment(K id, String fieldName) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.update(q,uOps,true);
    }

    @Override
    public UpdateResults decrement(K id, String fieldName,boolean createIfMiss) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.update(q,uOps,createIfMiss);
    }

    public UpdateResults decrement(K id, String fieldName) {
        return decrement(id,fieldName,false);
    }

    public UpdateResults decrement(K id, String fieldName,Number val) {
        return decrement(id,fieldName,val,false);
    }

    public UpdateResults decrement(K id, String fieldName,Number val,boolean createIfMiss) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.update(q,uOps,createIfMiss);
    }
    public UpdateResults decrement(K id, String fieldName,Number val,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        Map<String,Number> fieldsAndValues = new HashMap<String, Number>();
        fieldsAndValues.put(fieldName,val);
        return decrement(id,fieldsAndValues,whereField,whereFieldVal,fieldValidation,createIfMiss);
    }

    public UpdateResults decrement(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = createUpdateOperations();
        for(Map.Entry<String,Number> e : fieldsAndValues.entrySet()){
            uOps.dec(e.getKey(),e.getValue());
        }
        if(!fieldValidation)uOps.disableValidation();
        Query<T> query = createQuery();
        query.field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return ds.update(query,uOps,createIfMiss);
    }
    public UpdateResults decrement(K id, String fieldName,Number val,boolean fieldValidation,boolean createIfMiss) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName,val);
        if(!fieldValidation)uOps.disableValidation();
        Query<T> q = createQuery().field("_id").equal(id);
        return this.update(q,uOps,createIfMiss);
    }

    public UpdateResults decrement(K id, String fieldName,Number val,String whereField,Object whereFieldVal){
        return decrement(id,fieldName,val,whereField,whereFieldVal,true,false);
    }

    public UpdateResults decrement(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal){
        return decrement(id,fieldsAndValues,whereField,whereFieldVal,true,false);
    }

    @Override
    public T incrementAndGet(K id, String fieldName, Number val) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.findAndModify(q, uOps);
    }

    public T incrementAndGet(K id, Map<String,Number> fieldsAndValues){
        return incrementAndGet(id,fieldsAndValues,true,false,false);
    }

    public T incrementAndGet(K id, String fieldName, Number val,boolean fieldValidation,boolean createIfMiss) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        if(!fieldValidation)uOps.disableValidation();
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.findAndModify(q, uOps,false,createIfMiss);
    }

    public T incrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean createIfMiss){
        return incrementAndGet(id,fieldsAndValues,fieldValidation,false,createIfMiss);
    }

    public T incrementAndGet(K id, String incField,Number incVal,String whereField,Object whereFieldVal){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        Map<String,Number> incMap = new HashMap();
        incMap.put(incField,incVal);
        return incrementAndGet(q,incMap,true,false,false);
    }

    public T incrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return incrementAndGet(q,fieldsAndValues,true,false,false);
    }

    public T incrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss){
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return incrementAndGet(q, fieldsAndValues,fieldValidation,oldVersion,createIfMiss);
    }

    public T incrementAndGet(Query<T> q, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation)uOps.disableValidation();
        for(Map.Entry<String,Number> e : fieldsAndValues.entrySet()){
            uOps.inc(e.getKey(),e.getValue());
        }
        return this.ds.findAndModify(q, uOps,oldVersion,createIfMiss);
    }

    @Override
    public T incrementAndGet(K id, String fieldName, String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }
    public UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields){
        return incrementAndUpdate(id,incFields,updateFields,true,false);
    }
    public UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return incrementAndUpdate(q,incFields,updateFields,true,false);
    }
    public UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean createIfMiss){
        return incrementAndUpdate(id,incFields,updateFields,true,createIfMiss);
    }
    public UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id);
        return incrementAndUpdate(q,incFields,updateFields,fieldValidation,createIfMiss);
    }
    public UpdateResults incrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return incrementAndUpdate(q,incFields,updateFields,fieldValidation,createIfMiss);
    }
    public UpdateResults incrementAndUpdate(Query<T> query, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation)uOps.disableValidation();
        for(Map.Entry<String,Number> e : incFields.entrySet()){
            uOps.inc(e.getKey(),e.getValue());
        }
        for(Map.Entry<String,Object> e : updateFields.entrySet()){
            uOps.set(e.getKey(),e.getValue());
        }
        return update(query,uOps,createIfMiss);
    }

    @Override
    public T decrementAndGet(K id, String fieldName, String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }

    public T incrementAndGet(K id, String fieldName, Number val,String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }

    public T decrementAndGet(K id, String fieldName, Number val,String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }

    public T decrementAndGet(K id, Map<String,Number> fieldsAndValues){
        Query<T> q = createQuery().field("_id").equal(id);
        return decrementAndGet(q,fieldsAndValues,true,false,false);
    }

    public T decrementAndGet(K id, String fieldName, Number val,String whereField,Object whereFieldVal){
        Map<String,Number> map = new HashMap();
        map.put(fieldName,val);
        return decrementAndGet(id,map,whereField,whereFieldVal);
    }

    public T decrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return decrementAndGet(q,fieldsAndValues,true,false,false);
    }

    public T decrementAndGet(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return decrementAndGet(q,fieldsAndValues,true,false,createIfMiss);
    }

    public T decrementAndGet(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id);
        return decrementAndGet(q,fieldsAndValues,fieldValidation,oldVersion,createIfMiss);
    }

    public T decrementAndGet(Query<T> q, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean oldVersion,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation)uOps.disableValidation();
        for(Map.Entry<String,Number> e : fieldsAndValues.entrySet()){
            uOps.dec(e.getKey(),e.getValue());
        }
        return this.ds.findAndModify(q, uOps,oldVersion,createIfMiss);
    }

    public UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields){
        return decrementAndUpdate(id,incFields,updateFields,true,false);
    }
    public UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return decrementAndUpdate(q,incFields,updateFields,true,false);
    }
    public UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean createIfMiss){
        return decrementAndUpdate(id,incFields,updateFields,true,createIfMiss);
    }
    public UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id);
        return decrementAndUpdate(q,incFields,updateFields,fieldValidation,createIfMiss);
    }
    public UpdateResults decrementAndUpdate(K id, Map<String,Number> incFields,Map<String,Object> updateFields,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        Query<T> q = createQuery().field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return decrementAndUpdate(q,incFields,updateFields,fieldValidation,createIfMiss);
    }
    public UpdateResults decrementAndUpdate(Query<T> query, Map<String,Number> incFields,Map<String,Object> updateFields,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation)uOps.disableValidation();
        for(Map.Entry<String,Number> e : incFields.entrySet()){
            uOps.dec(e.getKey(),e.getValue());
        }
        for(Map.Entry<String,Object> e : updateFields.entrySet()){
            uOps.set(e.getKey(),e.getValue());
        }
        return update(query,uOps,createIfMiss);
    }

    @Override
    public UpdateResults increment(K id, String fieldName, Number val) {
        return increment(id,fieldName,val,false);
    }

    public UpdateResults increment(K id, String fieldName,boolean createIfMiss){
        return increment(id,fieldName,1,createIfMiss);
    }

    public UpdateResults increment(K id, String fieldName, Number val,boolean createIfMiss) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.update(q,uOps,createIfMiss);
    }

    public UpdateResults increment(K id, String fieldName, Number val,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        if(!fieldValidation)uOps.disableValidation();
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.update(q,uOps,createIfMiss);
    }

    public UpdateResults increment(K id, Map<String,Number> fieldsAndValues,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz);
        for(Map.Entry<String,Number> e : fieldsAndValues.entrySet()){
            uOps.inc(e.getKey(),e.getValue());
        }
        if(!fieldValidation)uOps.disableValidation();
        return update(id,uOps,createIfMiss);
    }

    public UpdateResults increment(K id, String fieldName, Number val,String whereField,Object whereFieldVal){
        return increment(id,fieldName,val,whereField,whereFieldVal,true,false);
    }

    public UpdateResults increment(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal){
        return increment(id,fieldsAndValues,whereField,whereFieldVal,true,false);
    }

    public UpdateResults increment(K id, String fieldName, Number val,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        Map<String,Number> incMap = new HashMap<String, Number>();
        incMap.put(fieldName,val);
        return increment(id,incMap,whereField,whereFieldVal,fieldValidation,createIfMiss);
    }
    public UpdateResults increment(K id, Map<String,Number> fieldsAndValues,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMiss){
        UpdateOperations<T> uOps = createUpdateOperations();
        for(Map.Entry<String,Number> e : fieldsAndValues.entrySet()){
            uOps.inc(e.getKey(),e.getValue());
        }
        if(!fieldValidation)uOps.disableValidation();
        Query<T> query = createQuery();
        query.field("_id").equal(id).field(whereField).equal(whereFieldVal);
        return ds.update(query,uOps,createIfMiss);
    }

    @Override
    public List<Object> distinct(String fieldName) {
        return this.getCollection().distinct(fieldName);
    }

    @Override
    public List<Object> distinct(String fieldName, Query<T> q) {
        return getCollection().distinct(fieldName, q.getQueryObject());
    }

    public UpdateResults addFieldValue(K id,String fieldName,Object value){
        return addFieldValue(id,fieldName,value,true);
    }

    public UpdateResults setFieldValue(K id,String fieldName,Object value){
        return setFieldValue(id,fieldName,value,true);
    }

    public UpdateResults addFieldValue(K id,String fieldName,Object value,boolean createIfMissing){
        UpdateOperations<T> up = createUpdateOperations();
        up.disableValidation();
        up.push(fieldName, value);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,createIfMissing,true);
    }

    public UpdateResults setFieldValue(K id, String fieldName, Object value, boolean createIfMissing){
        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.set(fieldName,value);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,createIfMissing);
    }

    public UpdateResults addFieldValueToSet(K id,String fieldName,Object value){
        return addFieldValueToSet(id,fieldName,value,true);
    }

    public UpdateResults addFieldValueToSet(K id,String fieldName,Object value,boolean createIfMissing){
        UpdateOperations<T> up = createUpdateOperations();
        up.disableValidation();
        up.addToSet(fieldName,value);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,createIfMissing,true);
    }

    public UpdateResults removeField(K id,String fieldName){
        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.unset(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFieldValue(K id,String fieldName,Object value){
        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeAll(fieldName,value);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFieldValues(K id,String fieldName,Object ... values){
        List<Object> list = new ArrayList<Object>();
        for(Object v : values){
            list.add(v);
        }

        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeAll(fieldName,list);

        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFieldValues(K id,String fieldName,Collection<?> values){
        List<Object> list = new ArrayList<Object>();
        for(Object v : values){
            list.add(v);
        }

        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeAll(fieldName,list);

        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFirstFieldValue(K id,String fieldName){
        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeFirst(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeLastFieldValue(K id,String fieldName){
        UpdateOperations<T> up = getDataStore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeLast(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public Object getFieldValue(K id,String fieldName){
        DBObject fields = new BasicDBObject();
        fields.put(fieldName,1);
        DBObject one = getCollection().findOne(id,fields);
        if(one == null){
            return null;
        }
        return one.get(fieldName);
    }

    public Object getFieldValueSlice(K id,String fieldName,int slice){
        DBObject fields = new BasicDBObject();
        DBObject sliceObj = new BasicDBObject();
        sliceObj.put("$slice",slice);
        fields.put(fieldName,sliceObj);
        DBObject one = getCollection().findOne(id,fields);
        if(one == null){
            return null;
        }
        return one.get(fieldName);
    }

    public Map<String,Object> getAsMap(K id){
        DBObject one = getCollection().findOne(id);
        if(one == null){
            return null;
        }
        return (Map<String,Object>)one;
    }

    public Map<String,Object> getAsMap(K id,String ... fields){
        if(fields != null){
            DBObject fieldsObj = new BasicDBObject();
            for(String f : fields){
                fieldsObj.put(f,1);
            }
            return (Map<String,Object>)getCollection().findOne(id,fieldsObj);
        }
        return (Map<String,Object>) getCollection().findOne(id);
    }

    public String getIdFieldName(){
        return mapping.getMapper().getMappedClass(entityClazz).getIdField().getName();
    }

    public Key<T> save(Map<String,Object> map){
        String idField = getIdFieldName();
        Object id = map.remove(idField);
        if(id == null){
            throw new InvalidParameterException("not id key["+idField+"] found,cannot save : " + map);
        }
        DBObject obj = new BasicDBObject();
        obj.put(Mapper.ID_KEY,id);
        obj.putAll(map);
        WriteResult result =  ds.save(getCollection(),obj);
        return new Key<T>(entityClazz, colName,result.getUpsertedId());
    }

    public Key<T> save(K id,Map<String,Object> map){
        DBObject obj = new BasicDBObject();
        obj.put(Mapper.ID_KEY,id);
        obj.putAll(map);

        WriteResult result =  ds.save(getCollection(),obj);
        return new Key<T>(entityClazz, colName,result.getUpsertedId());
    }

    public Key<T> save(DBObject obj){
        WriteResult result =  ds.save(getCollection(),obj);
        return new Key<T>(entityClazz, colName,result.getUpsertedId());
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClazz;
    }

    @Override
    public Key<T> save(final T entity) {
        return ds.save(colName,entity);
    }

    public <T> Iterable<Key<T>> save(final T... entities) {
        return ds.save(entities);
    }

    public <T> Iterable<Key<T>> save(final Iterable<T>  entities) {
        return ds.save(entities);
    }

    @Override
    public Key<T> insert(T entity) {
        return ds.insert(colName,entity);
    }

    public <T> Iterable<Key<T>> insert(Iterable<T> entities) {
        return ds.insert(colName,entities);
    }

    @Override
    public Key<T> save(final T entity, final WriteConcern wc) {
        return ds.save(colName,entity, new InsertOptions().writeConcern(wc));
    }

    @Override
    public UpdateResults update(final Query<T> query, final UpdateOperations<T> ops) {
        return ds.update(query, ops,false);
    }

    @Override
    public UpdateResults update(final K k, final UpdateOperations<T> ops) {
        Query<T> q = ds.find(colName,entityClazz ,"_id",k);
        return ds.update(q, ops,false);
    }

    @Override
    public UpdateResults update(K k, String fieldName, Object value) {
        return this.setFieldValue(k,fieldName,value,false);
    }
    public UpdateResults update(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal){
        return this.update(k,fieldAndValMap,whereField,whereFieldVal,true,false);
    }
    @Override
    public UpdateResults update(K k, Map<String, Object> fieldAndValMap) {
        return update(k,fieldAndValMap,true);
    }

    @Override
    public UpdateResults update(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation) {
        return update(k, fieldAndValMap,fieldValidation,false);
    }

    @Override
    public UpdateResults updateFirst(final Query<T> query, final UpdateOperations<T> ops) {
        return ds.update(query, ops, false);
    }

    public UpdateResults update(final Query<T> query, final UpdateOperations<T> ops,boolean createIfMissing) {
        return ds.update(query, ops,createIfMissing);
    }

    public UpdateResults update(final Query<T> query, final Map<String,Object> fieldAndValMap) {
        return update(query,fieldAndValMap,true,false,true);
    }

    public UpdateResults update(final Query<T> query, final UpdateOperations<T> ops,boolean createIfMissing,boolean multi) {
        return ds.update(query, ops,createIfMissing,multi);
    }

    public UpdateResults update(final K k, final UpdateOperations<T> ops,boolean createIfMissing) {
        Query<T> q = ds.find(colName,entityClazz ,"_id",k);
        return ds.update(q, ops,createIfMissing);
    }

    public UpdateResults update(K k, String fieldName, Object value,boolean createIfMissing) {
        return this.setFieldValue(k,fieldName,value,createIfMissing);
    }

    public UpdateResults update(Query<T> query, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing,boolean multi){
        UpdateOperations<T> ops = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation) ops.disableValidation();

        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        return ds.update(query,ops,createIfMissing,multi);
    }

    public UpdateResults update(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal,boolean fieldValidation,boolean createIfMissing){
        UpdateOperations<T> ops = createUpdateOperations();
        if(!fieldValidation) ops.disableValidation();
        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        Query<T> q = createQuery();
        q.field("_id").equal(k).field(whereField).equal(whereFieldVal);
        return ds.update(q, ops,createIfMissing);
    }

    public UpdateResults update(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing) {
        UpdateOperations<T> ops = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation) ops.disableValidation();
        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        Query<T> q = ds.find(colName,entityClazz ,"_id",k);
        return ds.update(q, ops,createIfMissing);
    }

    public T updateAndGet(K k, Map<String, Object> fieldAndValMap){
        return updateAndGet(k,fieldAndValMap,true,false,false);
    }

    public T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean createIfMissing){
        return updateAndGet(k,fieldAndValMap,true,false,createIfMissing);
    }

    public T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean createIfMissing){
        return updateAndGet(k,fieldAndValMap,fieldValidation,false,createIfMissing);
    }
    public T updateAndGet(K k, Map<String, Object> fieldAndValMap,String whereField,Object whereFieldVal){
        Query<T> q =createQuery().field("_id").equal(k).field(whereField).equal(whereFieldVal);
        return updateAndGet(q,fieldAndValMap,true,false,false);
    }
    public T updateAndGet(K k, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean oldVersion ,boolean createIfMissing){
        UpdateOperations<T> ops = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation) ops.disableValidation();
        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        Query<T> q = ds.find(colName,entityClazz ,"_id",k);
        return updateAndGet(q, ops,oldVersion,createIfMissing);
    }

    public T updateAndGet(Query<T> query, Map<String, Object> fieldAndValMap,boolean fieldValidation,boolean oldVersion ,boolean createIfMissing){
        UpdateOperations<T> ops = this.ds.createUpdateOperations(entityClazz);
        if(!fieldValidation) ops.disableValidation();
        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        return updateAndGet(query, ops,oldVersion,createIfMissing);
    }
    public T updateAndGet(Query<T> query, UpdateOperations<T> ops,boolean oldVersion ,boolean createIfMissing){
        return ds.findAndModify(query, ops,oldVersion,createIfMissing);
    }
    public UpdateResults updateFirst(final Query<T> query, final UpdateOperations<T> ops,boolean createIfMissing) {
        return ds.update(query, ops, createIfMissing);
    }

    @Override
    public WriteResult clear() {
        return deleteByQuery(createQuery());
    }

    public Class<T> getEntityClazz() {
        return entityClazz;
    }

    protected List<?> keysToIds(final List<Key<T>> keys) {
        final List<Object> ids = new ArrayList<Object>(keys.size() * 2);
        for (final Key<T> key : keys) {
            ids.add(key.getId());
        }
        return ids;
    }

    public AggregationPipeline createAggregation(){
        return ds.createAggregation(colName,entityClazz);
    }
}
