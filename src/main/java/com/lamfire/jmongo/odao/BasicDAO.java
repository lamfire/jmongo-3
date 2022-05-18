package com.lamfire.jmongo.odao;

import com.lamfire.jmongo.*;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.*;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "deprecation", "unused"})
class BasicDAO<T, K> implements DAO<T, K> {

    @Deprecated
    protected Class<T> entityClazz;

    @Deprecated
    protected DataStore ds;
    //CHECKSTYLE:ON


    public BasicDAO(final Class<T> entityClass, final MongoClient mongoClient, final Mapping mapping, final String dbName) {
        initDS(mongoClient, mapping, dbName);
        initType(mapping.getMapper(),entityClass);
    }


    @SuppressWarnings("unchecked")
    protected BasicDAO(final MongoClient mongoClient, final Mapping mapping, final String dbName) {
        initDS(mongoClient, mapping, dbName);
        initType(mapping.getMapper(),((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
    }

    @Override
    public long count() {
        return ds.getCount(entityClazz);
    }

    @Override
    public long count(final String key, final Object value) {
        return count(ds.find(entityClazz).filter(key, value));
    }

    @Override
    public long count(final Query<T> query) {
        return ds.getCount(query);
    }

    @Override
    public Query<T> createQuery() {
        return ds.find(entityClazz);
    }

    @Override
    public UpdateOperations<T> createUpdateOperations() {
        return ds.createUpdateOperations(entityClazz);
    }

    @Override
    public WriteResult delete(final T entity) {
        return ds.delete(entity);
    }

    @Override
    public WriteResult delete(final T entity, final WriteConcern wc) {
        return ds.delete(entity, wc);
    }

    @Override
    public WriteResult deleteById(final K id) {
        return ds.delete(entityClazz, id);
    }

    @Override
    public WriteResult deleteByQuery(final Query<T> query) {
        return ds.delete(query);
    }

    @Override
    public void ensureIndexes() {
        ds.ensureIndexes(entityClazz);
    }

    @Override
    public boolean exists(final String key, final Object value) {
        return exists(ds.find(entityClazz).filter(key, value));
    }

    @Override
    public boolean exists(final Query<T> query) {
        return query.get(new FindOptions().limit(1)) != null;
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
        return (List<K>) keysToIds(ds.find(entityClazz).asKeyList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<K> findIds(final String key, final Object value) {
        return (List<K>) keysToIds(ds.find(entityClazz).filter(key, value).asKeyList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<K> findIds(final Query<T> query) {
        return (List<K>) keysToIds(query.asKeyList());
    }

    @Override
    public T findOne(final String key, final Object value) {
        return ds.find(entityClazz).filter(key, value).get();
    }


    @Override
    public T findOne(final Query<T> query) {
        return query.get();
    }

    @Override
    public Key<T> findOneId() {
        return findOneId(ds.find(entityClazz));
    }

    @Override
    public Key<T> findOneId(final String key, final Object value) {
        return findOneId(ds.find(entityClazz).filter(key, value));
    }

    @Override
    public Key<T> findOneId(final Query<T> query) {
        Iterator<Key<T>> keys = query.fetchKeys().iterator();
        return keys.hasNext() ? keys.next() : null;
    }

    @Override
    public T get(final K id) {
        return ds.get(entityClazz, id);
    }

    @Override
    public DBCollection getCollection() {
        return ds.getCollection(entityClazz);
    }


    @Override
    public DataStore getDatastore() {
        return ds;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClazz;
    }

    @Override
    public Key<T> save(final T entity) {
        return ds.save(entity);
    }

    @Override
    public Key<T> save(final T entity, final WriteConcern wc) {
        return ds.save(entity, new InsertOptions().writeConcern(wc));
    }

    @Override
    public UpdateResults update(final Query<T> query, final UpdateOperations<T> ops) {
        return ds.update(query, ops);
    }

    @Override
    public UpdateResults updateFirst(final Query<T> query, final UpdateOperations<T> ops) {
        return ds.update(query, ops, new UpdateOptions());
    }


    @Deprecated
    public DataStore getDs() {
        return ds;
    }


    @Deprecated
    public Class<T> getEntityClazz() {
        return entityClazz;
    }

    protected void initDS(final MongoClient mongoClient, final Mapping mor, final String db) {
        ds = mor.createDatastore(mongoClient, db);
    }

    protected void initType(final Mapper mapper, final Class<T> type) {
        entityClazz = type;
        mapper.addMappedClass(type);
    }


    protected List<?> keysToIds(final List<Key<T>> keys) {
        final List<Object> ids = new ArrayList<Object>(keys.size() * 2);
        for (final Key<T> key : keys) {
            ids.add(key.getId());
        }
        return ids;
    }

}
