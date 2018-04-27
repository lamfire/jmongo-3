package com.lamfire.jmongo;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.mongodb.DBObject;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ObjectFactory {

    <T> T createInstance(Class<T> clazz);


    <T> T createInstance(Class<T> clazz, DBObject dbObj);


    Object createInstance(Mapper mapper, MappedField mf, DBObject dbObj);


    List createList(MappedField mf);


    Map createMap(MappedField mf);


    Set createSet(MappedField mf);
}
