package com.lamfire.jmongo.query;

import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.mapping.Mapper;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class QueryKeyIterator<T> extends QueryIterator<T, Key<T>> {

    public QueryKeyIterator(final Datastore datastore, final DBCursor cursor, final Mapper mapper,
                            final Class<T> clazz, final String collection) {
        super(datastore, cursor, mapper, clazz, collection, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Key<T> convertItem(final DBObject dbObj) {
        Object id = dbObj.get(Mapper.ID_KEY);
        if (id instanceof DBObject) {
            Class type = getMapper().getMappedClass(getClazz()).getMappedIdField().getType();
            id = getMapper().fromDBObject(getDatastore(), type, (DBObject) id, getMapper().createEntityCache());
        }
        return new Key<T>(getClazz(), getCollection(), id);
    }
}
