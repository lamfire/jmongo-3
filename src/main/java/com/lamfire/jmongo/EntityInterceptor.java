package com.lamfire.jmongo;


import com.lamfire.jmongo.mapping.Mapper;
import com.mongodb.DBObject;



public interface EntityInterceptor {

    void postLoad(Object ent, DBObject dbObj, Mapper mapper);


    void postPersist(Object ent, DBObject dbObj, Mapper mapper);


    void preLoad(Object ent, DBObject dbObj, Mapper mapper);


    void prePersist(Object ent, DBObject dbObj, Mapper mapper);


    void preSave(Object ent, DBObject dbObj, Mapper mapper);
}
