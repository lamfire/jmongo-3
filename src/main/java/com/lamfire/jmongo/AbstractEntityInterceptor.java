package com.lamfire.jmongo;


import com.lamfire.jmongo.mapping.Mapper;
import com.mongodb.DBObject;

public class AbstractEntityInterceptor implements EntityInterceptor {

    @Override
    public void postLoad(final Object ent, final DBObject dbObj, final Mapper mapper) {
    }

    @Override
    public void postPersist(final Object ent, final DBObject dbObj, final Mapper mapper) {
    }

    @Override
    public void preLoad(final Object ent, final DBObject dbObj, final Mapper mapper) {
    }

    @Override
    public void prePersist(final Object ent, final DBObject dbObj, final Mapper mapper) {
    }

    @Override
    public void preSave(final Object ent, final DBObject dbObj, final Mapper mapper) {
    }
}
