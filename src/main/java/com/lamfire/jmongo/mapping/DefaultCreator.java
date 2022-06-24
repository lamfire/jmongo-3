package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.ObjectFactory;
import com.lamfire.jmongo.annotations.ConstructorArgs;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.mongodb.DBObject;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



public class DefaultCreator implements ObjectFactory {

    private static final Logger LOG = LoggerFactory.get(DefaultCreator.class);

    private Map<String, Class> classNameCache = new ConcurrentHashMap<String, Class>();

    private MapperOptions options = null;


    public DefaultCreator() {
    }


    public DefaultCreator(final MapperOptions options) {
        this.options = options;
    }

    private static <T> Constructor<T> getNoArgsConstructor(final Class<T> type) {
        try {
            final Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new MappingException("No usable constructor for " + type.getName(), e);
        }
    }


    @Deprecated
    public <T> T createInst(final Class<T> clazz) {
        return createInstance(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(final Class<T> clazz) {
        try {
            return getNoArgsConstructor(clazz).newInstance();
        } catch (Exception e) {
            if (Collection.class.isAssignableFrom(clazz)) {
                return (T) createList(null);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return (T) createMap(null);
            } else if (Set.class.isAssignableFrom(clazz)) {
                return (T) createSet(null);
            }
            throw new MappingException("No usable constructor for " + clazz.getName(), e);
        }
    }

    @Override
    public <T> T createInstance(final Class<T> clazz, final DBObject dbObj) {
        Class<T> c = getClass(dbObj);
        if (c == null) {
            c = clazz;
        }
        return createInstance(c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object createInstance(final Mapper mapper, final MappedField mf, final DBObject dbObj) {
        Class c = getClass(dbObj);
        if (c == null) {
            c = mf.isSingleValue() ? mf.getConcreteType() : mf.getSubClass();
            if (c.equals(Object.class)) {
                c = mf.getConcreteType();
            }
        }
        try {
            return createInstance(c, dbObj);
        } catch (RuntimeException e) {
            final ConstructorArgs argAnn = mf.getAnnotation(ConstructorArgs.class);
            if (argAnn == null) {
                throw e;
            }
            //TODO: now that we have a mapper, get the arg types that way by getting the fields by name. + Validate names
            final Object[] args = new Object[argAnn.value().length];
            final Class[] argTypes = new Class[argAnn.value().length];
            for (int i = 0; i < argAnn.value().length; i++) {
                // TODO: run converters and stuff against these. Kinda like the List of List stuff,
                // using a fake MappedField to hold the value
                final Object val = dbObj.get(argAnn.value()[i]);
                args[i] = val;
                argTypes[i] = val.getClass();
            }
            try {
                final Constructor constructor = c.getDeclaredConstructor(argTypes);
                constructor.setAccessible(true);
                return constructor.newInstance(args);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List createList(final MappedField mf) {
        return newInstance(mf != null ? mf.getCTor() : null, ArrayList.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map createMap(final MappedField mf) {
        return newInstance(mf != null ? mf.getCTor() : null, HashMap.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set createSet(final MappedField mf) {
        return newInstance(mf != null ? mf.getCTor() : null, HashSet.class);
    }


    public Map<String, Class> getClassNameCache() {
        HashMap<String, Class> copy = new HashMap<String, Class>();
        copy.putAll(classNameCache);
        return copy;
    }

    protected ClassLoader getClassLoaderForClass() {
        return Thread.currentThread().getContextClassLoader();
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getClass(final DBObject dbObj) {
        // see if there is a className value
        Class c = null;
        if (dbObj.containsField(Mapper.CLASS_NAME_FIELDNAME)) {
            final String className = (String) dbObj.get(Mapper.CLASS_NAME_FIELDNAME);
            // try to Class.forName(className) as defined in the dbObject first,
            // otherwise return the entityClass
            try {
                if (options != null && options.isCacheClassLookups()) {
                    c = classNameCache.get(className);
                    if (c == null) {
                        c = Class.forName(className, true, getClassLoaderForClass());
                        classNameCache.put(className, c);
                    }
                } else {
                    c = Class.forName(className, true, getClassLoaderForClass());
                }
            } catch (ClassNotFoundException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warning("Class not found defined in dbObj: ", e);
                }
            }
        }
        return c;
    }


    private <T> T newInstance(final Constructor<T> tryMe, final Class<T> fallbackType) {
        if (tryMe != null) {
            tryMe.setAccessible(true);
            try {
                return tryMe.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return createInstance(fallbackType);
    }

}
