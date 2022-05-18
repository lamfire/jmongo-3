package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.mongodb.DBObject;

import java.util.Map;



public interface CustomMapper {

    void fromDBObject(final DataStore datastore, DBObject dbObject, MappedField mf, Object entity, EntityCache cache, Mapper mapper);


    void toDBObject(Object entity, MappedField mf, DBObject dbObject, Map<Object, DBObject> involvedObjects, Mapper mapper);
}
