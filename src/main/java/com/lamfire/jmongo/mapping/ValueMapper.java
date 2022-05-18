package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.mongodb.DBObject;

import java.util.Map;



class ValueMapper implements CustomMapper {
    @Override
    public void fromDBObject(final DataStore datastore, final DBObject dbObject, final MappedField mf, final Object entity,
                             final EntityCache cache, final Mapper mapper) {
        mapper.getConverters().fromDBObject(dbObject, mf, entity);
    }

    @Override
    public void toDBObject(final Object entity, final MappedField mf, final DBObject dbObject, final Map<Object, DBObject> involvedObjects,
                           final Mapper mapper) {
        try {
            mapper.getConverters().toDBObject(entity, mf, dbObject, mapper.getOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
