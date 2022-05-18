package com.lamfire.jmongo;


import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.annotations.Entity;
import com.lamfire.jmongo.logging.JmongoLoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.MappingException;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.lamfire.jmongo.utils.ReflectionUtils;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;


public class Mapping {
    private static final Logger LOG = JmongoLoggerFactory.get(Mapping.class);
    private final Mapper mapper;


    public Mapping() {
        this(new Mapper(), Collections.<Class>emptySet());
    }


    public Mapping(final Mapper mapper, final Set<Class> classesToMap) {
        this.mapper = mapper;
        for (final Class c : classesToMap) {
            map(c);
        }
    }


    public Mapping(final Mapper mapper) {
        this(mapper, Collections.<Class>emptySet());
    }


    public Mapping(final Set<Class> classesToMap) {
        this(new Mapper(), classesToMap);
    }


    @SuppressWarnings("deprecation")
    public DataStore createDatastore(final MongoClient mongoClient, final String dbName) {
        return new DataStoreImpl(this, mongoClient, dbName);
    }


    @SuppressWarnings("deprecation")
    public DataStore createDatastore(final MongoClient mongoClient, final Mapper mapper, final String dbName) {
        return new DataStoreImpl(this, mapper, mongoClient, dbName);
    }


    public <T> T fromDBObject(final DataStore datastore, final Class<T> entityClass, final DBObject dbObject) {
        return fromDBObject(datastore, entityClass, dbObject, mapper.createEntityCache());
    }


    public <T> T fromDBObject(final DataStore datastore, final Class<T> entityClass, final DBObject dbObject, final EntityCache cache) {
        if (!entityClass.isInterface() && !mapper.isMapped(entityClass)) {
            throw new MappingException("Trying to map to an unmapped class: " + entityClass.getName());
        }
        try {
            return mapper.fromDBObject(datastore, entityClass, dbObject, cache);
        } catch (Exception e) {
            throw new MappingException("Could not map entity from DBObject", e);
        }
    }


    public Mapper getMapper() {
        return mapper;
    }


    @Deprecated
    public boolean getUseBulkWriteOperations() {
        return false;
    }


    public boolean isMapped(final Class entityClass) {
        return mapper.isMapped(entityClass);
    }


    @Deprecated
    public boolean isUseBulkWriteOperations() {
        return false;
    }


    @Deprecated
    public void setUseBulkWriteOperations(final boolean useBulkWriteOperations) {
    }


    public synchronized Mapping map(final Class... entityClasses) {
        if (entityClasses != null && entityClasses.length > 0) {
            for (final Class entityClass : entityClasses) {
                if (!mapper.isMapped(entityClass)) {
                    mapper.addMappedClass(entityClass);
                }
            }
        }
        return this;
    }


    public synchronized Mapping map(final Set<Class> entityClasses) {
        if (entityClasses != null && !entityClasses.isEmpty()) {
            for (final Class entityClass : entityClasses) {
                if (!mapper.isMapped(entityClass)) {
                    mapper.addMappedClass(entityClass);
                }
            }
        }
        return this;
    }


    public synchronized Mapping mapPackage(final String packageName) {
        return mapPackage(packageName, false);
    }


    public synchronized Mapping mapPackage(final String packageName, final boolean ignoreInvalidClasses) {
        try {
            for (final Class clazz : ReflectionUtils.getClasses(packageName, mapper.getOptions().isMapSubPackages())) {
                try {
                    final Embedded embeddedAnn = ReflectionUtils.getClassEmbeddedAnnotation(clazz);
                    final Entity entityAnn = ReflectionUtils.getClassEntityAnnotation(clazz);
                    final boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
                    if ((entityAnn != null || embeddedAnn != null) && !isAbstract) {
                        map(clazz);
                    }
                } catch (final MappingException ex) {
                    if (!ignoreInvalidClasses) {
                        throw ex;
                    }
                }
            }
            return this;
        } catch (IOException e) {
            throw new MappingException("Could not get map classes from package " + packageName, e);
        } catch (ClassNotFoundException e) {
            throw new MappingException("Could not get map classes from package " + packageName, e);
        }
    }


    public Mapping mapPackageFromClass(final Class clazz) {
        return mapPackage(clazz.getPackage().getName(), false);
    }


    public DBObject toDBObject(final Object entity) {
        try {
            return mapper.toDBObject(entity);
        } catch (Exception e) {
            throw new MappingException("Could not map entity to DBObject", e);
        }
    }
}
