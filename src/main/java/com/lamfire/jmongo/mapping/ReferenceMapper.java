package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.annotations.Reference;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.lamfire.jmongo.mapping.lazy.LazyFeatureDependencies;
import com.lamfire.jmongo.mapping.lazy.proxy.ProxiedEntityReference;
import com.lamfire.jmongo.mapping.lazy.proxy.ProxiedEntityReferenceList;
import com.lamfire.jmongo.mapping.lazy.proxy.ProxiedEntityReferenceMap;
import com.lamfire.jmongo.mapping.lazy.proxy.ProxyHelper;
import com.lamfire.jmongo.utils.IterHelper;
import com.lamfire.jmongo.utils.IterHelper.IterCallback;
import com.lamfire.jmongo.utils.IterHelper.MapIterCallback;
import com.lamfire.jmongo.utils.ReflectionUtils;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unchecked", "rawtypes"})
class ReferenceMapper implements CustomMapper {
    public static final Logger LOG = LoggerFactory.get(ReferenceMapper.class);

    @Override
    public void fromDBObject(final DataStore datastore, final DBObject dbObject, final MappedField mf, final Object entity,
                             final EntityCache cache, final Mapper mapper) {
        final Class fieldType = mf.getType();

        final Reference refAnn = mf.getAnnotation(Reference.class);
        if (mf.isMap()) {
            readMap(datastore, mapper, entity, refAnn, cache, mf, dbObject);
        } else if (mf.isMultipleValues()) {
            readCollection(datastore, mapper, dbObject, mf, entity, refAnn, cache);
        } else {
            readSingle(datastore, mapper, entity, fieldType, refAnn, cache, mf, dbObject);
        }

    }

    @Override
    public void toDBObject(final Object entity, final MappedField mf, final DBObject dbObject, final Map<Object, DBObject> involvedObjects,
                           final Mapper mapper) {
        final String name = mf.getNameToStore();

        final Object fieldValue = mf.getFieldValue(entity);

        if (fieldValue == null && !mapper.getOptions().isStoreNulls()) {
            return;
        }

        final Reference refAnn = mf.getAnnotation(Reference.class);
        if (mf.isMap()) {
            writeMap(mf, dbObject, name, fieldValue, refAnn, mapper);
        } else if (mf.isMultipleValues()) {
            writeCollection(mf, dbObject, name, fieldValue, refAnn, mapper);
        } else {
            writeSingle(dbObject, name, fieldValue, refAnn, mapper);
        }

    }

    private void addValue(final List values, final Object o, final Mapper mapper, final boolean idOnly) {
        if (o == null && mapper.getOptions().isStoreNulls()) {
            values.add(null);
            return;
        }

        final Key key = o instanceof Key
                        ? (Key) o
                        : getKey(o, mapper);
        values.add(idOnly
                   ? mapper.keyToId(key)
                   : mapper.keyToDBRef(key));
    }

    private Object createOrReuseProxy(final DataStore datastore, final Mapper mapper, final Class referenceObjClass, final Object ref,
                                      final EntityCache cache, final Reference anntotation) {
        final Key key = anntotation.idOnly() ? mapper.manualRefToKey(referenceObjClass, ref) : mapper.refToKey((DBRef) ref);
        final Object proxyAlreadyCreated = cache.getProxy(key);
        if (proxyAlreadyCreated != null) {
            return proxyAlreadyCreated;
        }
        final Object newProxy = mapper.getProxyFactory().createProxy(datastore, referenceObjClass, key, anntotation.ignoreMissing());
        cache.putProxy(key, newProxy);
        return newProxy;
    }

    private Key<?> getKey(final Object entity, final Mapper mapper) {
        try {
            if (entity instanceof ProxiedEntityReference) {
                final ProxiedEntityReference proxy = (ProxiedEntityReference) entity;
                return proxy.__getKey();
            }
            final MappedClass mappedClass = mapper.getMappedClass(entity);
            Object id = mappedClass.getIdField().get(entity);
            if (id == null) {
                throw new MappingException("@Id field cannot be null!");
            }
            return new Key(mappedClass.getClazz(), mappedClass.getCollectionName(), id);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }

    private void readCollection(final DataStore datastore, final Mapper mapper, final DBObject dbObject, final MappedField mf,
                                final Object entity,
                                final Reference refAnn,
                                final EntityCache cache) {
        // multiple references in a List
        final Class referenceObjClass = mf.getSubClass();
        // load reference class.  this "fixes" #816
        mapper.getMappedClass(referenceObjClass);
        Collection references = mf.isSet() ? mapper.getOptions().getObjectFactory().createSet(mf)
                                           : mapper.getOptions().getObjectFactory().createList(mf);

        if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
            final Object dbVal = mf.getDbObjectValue(dbObject);
            if (dbVal != null) {
                references = mapper.getProxyFactory()
                                   .createListProxy(datastore, references, referenceObjClass, refAnn.ignoreMissing());
                final ProxiedEntityReferenceList referencesAsProxy = (ProxiedEntityReferenceList) references;

                if (dbVal instanceof List) {
                    referencesAsProxy.__addAll(refAnn.idOnly()
                                               ? mapper.getKeysByManualRefs(referenceObjClass, (List) dbVal)
                                               : mapper.getKeysByRefs((List) dbVal));
                } else {
                    referencesAsProxy.__add(refAnn.idOnly()
                                            ? mapper.manualRefToKey(referenceObjClass, dbVal)
                                            : mapper.refToKey((DBRef) dbVal));
                }
            }
        } else {
            final Object dbVal = mf.getDbObjectValue(dbObject);
            final Collection refs = references;
            new IterHelper<String, Object>().loopOrSingle(dbVal, new IterCallback<Object>() {
                @Override
                public void eval(final Object val) {
                    final Object ent = resolveObject(datastore, mapper, cache, mf, refAnn.idOnly(), val);
                    if (ent == null) {
                        LOG.warning("Null reference found when retrieving value for " + mf.getFullName());
                    } else {
                        refs.add(ent);
                    }
                }
            });
        }

        if (mf.getType().isArray()) {
            mf.setFieldValue(entity, ReflectionUtils.convertToArray(mf.getSubClass(), ReflectionUtils.iterToList(references)));
        } else {
            mf.setFieldValue(entity, references);
        }
    }

    private void readMap(final DataStore datastore, final Mapper mapper, final Object entity, final Reference refAnn,
                         final EntityCache cache, final MappedField mf, final DBObject dbObject) {
        final Class referenceObjClass = mf.getSubClass();
        Map m = mapper.getOptions().getObjectFactory().createMap(mf);

        final DBObject dbVal = (DBObject) mf.getDbObjectValue(dbObject);
        if (dbVal != null) {
            if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                // replace map by proxy to it.
                m = mapper.getProxyFactory().createMapProxy(datastore, m, referenceObjClass, refAnn.ignoreMissing());
            }

            final Map map = m;
            new IterHelper<Object, Object>().loopMap(dbVal, new MapIterCallback<Object, Object>() {
                @Override
                public void eval(final Object k, final Object val) {

                    final Object objKey = mapper.getConverters().decode(mf.getMapKeyClass(), k, mf);

                    if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                        final ProxiedEntityReferenceMap proxiedMap = (ProxiedEntityReferenceMap) map;
                        proxiedMap.__put(objKey, refAnn.idOnly()
                                                 ? mapper.manualRefToKey(referenceObjClass, val)
                                                 : mapper.refToKey((DBRef) val));
                    } else {
                        map.put(objKey, resolveObject(datastore, mapper, cache, mf, refAnn.idOnly(), val));
                    }
                }
            });
        }
        mf.setFieldValue(entity, m);
    }

    private void readSingle(final DataStore datastore, final Mapper mapper, final Object entity, final Class fieldType,
                            final Reference annotation, final EntityCache cache, final MappedField mf, final DBObject dbObject) {

        final Object ref = mf.getDbObjectValue(dbObject);
        if (ref != null) {
            Object resolvedObject;
            if (annotation.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                resolvedObject = createOrReuseProxy(datastore, mapper, fieldType, ref, cache, annotation);
            } else {
                resolvedObject = resolveObject(datastore, mapper, cache, mf, annotation.idOnly(), ref);
            }

            if (resolvedObject != null) {
                mf.setFieldValue(entity, resolvedObject);
            }

        }
    }

    private void writeCollection(final MappedField mf, final DBObject dbObject, final String name, final Object fieldValue,
                                 final Reference refAnn, final Mapper mapper) {
        if (fieldValue != null) {
            final List values = new ArrayList();

            if (ProxyHelper.isProxy(fieldValue) && ProxyHelper.isUnFetched(fieldValue)) {
                final ProxiedEntityReferenceList p = (ProxiedEntityReferenceList) fieldValue;
                final List<Key<?>> getKeysAsList = p.__getKeysAsList();
                for (final Key<?> key : getKeysAsList) {
                    addValue(values, key, mapper, refAnn.idOnly());
                }
            } else {

                if (mf.getType().isArray()) {
                    for (final Object o : (Object[]) fieldValue) {
                        addValue(values, o, mapper, refAnn.idOnly());
                    }
                } else {
                    for (final Object o : (Iterable) fieldValue) {
                        addValue(values, o, mapper, refAnn.idOnly());
                    }
                }
            }
            if (!values.isEmpty() || mapper.getOptions().isStoreEmpties()) {
                dbObject.put(name, values);
            }
        }
    }

    private void writeMap(final MappedField mf, final DBObject dbObject, final String name, final Object fieldValue,
                          final Reference refAnn, final Mapper mapper) {
        final Map<Object, Object> map = (Map<Object, Object>) fieldValue;
        if ((map != null)) {
            final Map values = mapper.getOptions().getObjectFactory().createMap(mf);

            if (ProxyHelper.isProxy(map) && ProxyHelper.isUnFetched(map)) {
                final ProxiedEntityReferenceMap proxy = (ProxiedEntityReferenceMap) map;

                final Map<Object, Key<?>> refMap = proxy.__getReferenceMap();
                for (final Map.Entry<Object, Key<?>> entry : refMap.entrySet()) {
                    final Object key = entry.getKey();
                    values.put(key, refAnn.idOnly()
                                    ? mapper.keyToId(entry.getValue())
                                    : mapper.keyToDBRef(entry.getValue()));
                }
            } else {
                for (final Map.Entry<Object, Object> entry : map.entrySet()) {
                    final String strKey = mapper.getConverters().encode(entry.getKey()).toString();
                    values.put(strKey, refAnn.idOnly()
                                       ? mapper.keyToId(getKey(entry.getValue(), mapper))
                                       : mapper.keyToDBRef(getKey(entry.getValue(), mapper)));
                }
            }
            if (!values.isEmpty() || mapper.getOptions().isStoreEmpties()) {
                dbObject.put(name, values);
            }
        }
    }

    private void writeSingle(final DBObject dbObject, final String name, final Object fieldValue, final Reference refAnn,
                             final Mapper mapper) {
        if (fieldValue == null) {
            if (mapper.getOptions().isStoreNulls()) {
                dbObject.put(name, null);
            }
        } else {
            Key<?> key = getKey(fieldValue, mapper);
            if (refAnn.idOnly()) {
                Object id = mapper.keyToId(key);
                if (id != null && mapper.isMapped(id.getClass())) {
                    id = mapper.toMongoObject(id, true);
                }

                dbObject.put(name, id);
            } else {
                dbObject.put(name, mapper.keyToDBRef(key));
            }
        }
    }

    Object resolveObject(final DataStore datastore, final Mapper mapper, final EntityCache cache, final MappedField mf,
                         final boolean idOnly, final Object ref) {
        if (ref == null) {
            return null;
        }

        final DBRef dbRef = idOnly ? null : (DBRef) ref;
        final Key key = mapper.createKey(mf.isSingleValue() ? mf.getType() : mf.getSubClass(),
                                         idOnly ? ref : dbRef.getId());

        final Object cached = cache.getEntity(key);
        if (cached != null) {
            return cached;
        }

        final DBObject refDbObject;
        DBCollection collection;
        Object id;

        if (idOnly) {
            collection = datastore.getCollection(key.getType());
            id = ref;
        } else {
            collection = datastore.getDB().getCollection(dbRef.getCollectionName());
            id = dbRef.getId();
        }
        if (id instanceof DBObject) {
            ((DBObject) id).removeField(Mapper.CLASS_NAME_FIELDNAME);
        }
        refDbObject = collection.findOne(id);

        if (refDbObject != null) {
            Object refObj = mapper.getOptions().getObjectFactory().createInstance(mapper, mf, refDbObject);
            refObj = mapper.fromDb(datastore, refDbObject, refObj, cache);
            cache.putEntity(key, refObj);
            return refObj;
        }

        final boolean ignoreMissing = mf.getAnnotation(Reference.class) != null && mf.getAnnotation(Reference.class).ignoreMissing();
        if (!ignoreMissing) {
            throw new MappingException("The reference(" + ref.toString() + ") could not be fetched for " + mf.getFullName());
        } else {
            return null;
        }
    }
}
