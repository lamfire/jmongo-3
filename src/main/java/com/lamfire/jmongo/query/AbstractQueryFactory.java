package com.lamfire.jmongo.query;

import com.lamfire.jmongo.Datastore;
import com.mongodb.DBCollection;


public abstract class AbstractQueryFactory implements QueryFactory {

    @Override
    public <T> Query<T> createQuery(final Datastore datastore, final DBCollection collection, final Class<T> type) {
        return createQuery(datastore, collection, type, null);
    }

    @Override
    public <T> Query<T> createQuery(final Datastore datastore) {
        return new QueryImpl<T>(null, null, datastore);
    }
}
