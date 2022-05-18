package com.lamfire.jmongo.query;

import com.lamfire.jmongo.DataStore;
import com.mongodb.DBCollection;


public abstract class AbstractQueryFactory implements QueryFactory {

    @Override
    public <T> Query<T> createQuery(final DataStore datastore, final DBCollection collection, final Class<T> type) {
        return createQuery(datastore, collection, type, null);
    }

    @Override
    public <T> Query<T> createQuery(final DataStore datastore) {
        return new QueryImpl<T>(null, null, datastore);
    }
}
