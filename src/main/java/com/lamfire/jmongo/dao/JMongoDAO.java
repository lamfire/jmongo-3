package com.lamfire.jmongo.dao;

import com.lamfire.jmongo.*;
import com.lamfire.jmongo.InsertOptions;
import com.lamfire.jmongo.aggregation.AggregationPipeline;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.*;
import com.mongodb.*;

import java.util.*;


public class JMongoDAO<T, K> implements DAO<T, K> {
    private String dbName;
    private String colName;
    protected Class<T> entityClazz;

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
    public Datastore getDatastore() {
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

    @Override
    public void increment(K id, String fieldName) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        this.ds.update(q,uOps);
    }

    @Override
    public void decrement(K id, String fieldName) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        this.ds.update(q,uOps);
    }

    @Override
    public T incrementAndGet(K id, String fieldName, Number val) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return this.ds.findAndModify(q, uOps);
    }

    @Override
    public T incrementAndGet(K id, String fieldName, String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }

    @Override
    public T decrementAndGet(K id, String fieldName, String... includeFields) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).dec(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id).includeFieldsOnly(includeFields);
        return this.ds.findAndModify(q, uOps);
    }

    @Override
    public void increment(K id, String fieldName, Number val) {
        UpdateOperations<T> uOps = this.ds.createUpdateOperations(entityClazz).inc(fieldName,val);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        this.ds.update(q,uOps);
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
        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
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
        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.unset(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFieldValue(K id,String fieldName,Object value){
        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
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

        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
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

        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeAll(fieldName,list);

        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeFirstFieldValue(K id,String fieldName){
        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
        up.disableValidation();
        up.removeFirst(fieldName);
        Query<T> q = ds.find(colName,entityClazz ,"_id",id);
        return ds.update(q,up,false);
    }

    public UpdateResults removeLastFieldValue(K id,String fieldName){
        UpdateOperations<T> up = getDatastore().createUpdateOperations(entityClazz);
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
        DBObject one = null;
        if(fields != null){
            DBObject fieldsObj = new BasicDBObject();
            for(String f : fields){
                fieldsObj.put(f,1);
            }
            one = getCollection().findOne(id,fieldsObj);
        }else{
            one = getCollection().findOne(id);
        }

        if(one == null){
            return null;
        }
        return (Map<String,Object>)one;
    }

    public Key<T> save(Map<String,Object> map){
        DBObject obj = new BasicDBObject();
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

    @Override
    public Key<T> insert(T entity) {
        return ds.insert(colName,entity);
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

    @Override
    public UpdateResults update(K k, Map<String, Object> fieldAndValMap) {
        Key<T> key = new Key<T>(entityClazz, colName, k);
        UpdateOperations<T> ops = this.ds.createUpdateOperations(entityClazz);
        ops.disableValidation();
        for(Map.Entry<String,Object> e : fieldAndValMap.entrySet()){
            ops.set(e.getKey(), e.getValue());
        }
        Query<T> q = ds.find(colName,entityClazz ,"_id",k);
        return ds.update(q, ops,false);
    }

    @Override
    public UpdateResults updateFirst(final Query<T> query, final UpdateOperations<T> ops) {
        return ds.update(query, ops, false);
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
