package com.lamfire.jmongo.query;

import com.lamfire.jmongo.DataStore;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


public class DefaultQueryFactory extends AbstractQueryFactory {

    @Override
    public <T> Query<T> createQuery(final DataStore datastore, final DBCollection collection, final Class<T> type, final DBObject query) {

        final QueryImpl<T> item = new QueryImpl<T>(type, collection, datastore);

        if (query != null) {
            item.setQueryObject(query);
        }

        return item;
    }

}
