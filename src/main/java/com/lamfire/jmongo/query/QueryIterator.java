package com.lamfire.jmongo.query;


import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class QueryIterator<T, V> implements Iterable<V>, Iterator<V> {
    private final Iterator<DBObject> wrapped;
    private final Mapper mapper;
    private final Class<T> clazz;
    private final String collection;
    private final EntityCache cache;
    private long driverTime;
    private long mapperTime;
    private Datastore datastore;


    public QueryIterator(final Datastore datastore, final Iterator<DBObject> it, final Mapper mapper, final Class<T> clazz,
                         final String collection, final EntityCache cache) {
        wrapped = it;
        this.mapper = mapper;
        this.clazz = clazz;
        this.collection = collection;
        this.cache = cache;
        this.datastore = datastore;
    }


    public void close() {
        if (wrapped != null && wrapped instanceof DBCursor) {
            ((DBCursor) wrapped).close();
        }
    }


    public Class<T> getClazz() {
        return clazz;
    }


    public String getCollection() {
        return collection;
    }


    public DBCursor getCursor() {
        return (DBCursor) wrapped;
    }


    public long getDriverTime() {
        return driverTime;
    }


    public Mapper getMapper() {
        return mapper;
    }


    public long getMapperTime() {
        return mapperTime;
    }

    @Override
    public boolean hasNext() {
        if (wrapped == null) {
            return false;
        }
        final long start = System.currentTimeMillis();
        final boolean ret = wrapped.hasNext();
        driverTime += System.currentTimeMillis() - start;
        return ret;
    }

    @Override
    public V next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final DBObject dbObj = getNext();
        return processItem(dbObj);
    }

    @Override
    public void remove() {
        final long start = System.currentTimeMillis();
        wrapped.remove();
        driverTime += System.currentTimeMillis() - start;
    }

    @Override
    public Iterator<V> iterator() {
        return this;
    }

    @SuppressWarnings("unchecked")
    protected V convertItem(final DBObject dbObj) {
        return (V) mapper.fromDBObject(datastore, clazz, dbObj, cache);
    }

    protected DBObject getNext() {
        final long start = System.currentTimeMillis();
        final DBObject dbObj = wrapped.next();
        driverTime += System.currentTimeMillis() - start;
        return dbObj;
    }

    private V processItem(final DBObject dbObj) {
        final long start = System.currentTimeMillis();
        final V item = convertItem(dbObj);
        mapperTime += System.currentTimeMillis() - start;
        return item;
    }

    Datastore getDatastore() {
        return datastore;
    }
}
