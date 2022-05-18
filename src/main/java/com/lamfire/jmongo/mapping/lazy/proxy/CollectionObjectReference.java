package com.lamfire.jmongo.mapping.lazy.proxy;

import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;


public class CollectionObjectReference<T> extends AbstractReference implements ProxiedEntityReferenceList {

    private static final long serialVersionUID = 1L;
    private final List<Key<?>> listOfKeys;


    public CollectionObjectReference(final Collection<T> type, final Class<T> referenceObjClass, final boolean ignoreMissing,
                                     final DataStore datastore) {

        super(datastore, referenceObjClass, ignoreMissing);

        object = type;
        listOfKeys = new ArrayList<Key<?>>();
    }

    @Override
    //CHECKSTYLE:OFF
    public void __add(final Key key) {
        //CHECKSTYLE:ON
        listOfKeys.add(key);
    }

    @Override
    //CHECKSTYLE:OFF
    public void __addAll(final Collection<? extends Key<?>> keys) {
        //CHECKSTYLE:ON
        listOfKeys.addAll(keys);
    }

    //CHECKSTYLE:OFF
    @Override
    public List<Key<?>> __getKeysAsList() {
        return Collections.unmodifiableList(listOfKeys);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void beforeWriteObject() {
        if (__isFetched()) {
            syncKeys();
            ((Collection<T>) object).clear();
        }
    }
    //CHECKSTYLE:ON

    @Override
    @SuppressWarnings("unchecked")
    protected synchronized Object fetch() {
        final Collection<T> c = (Collection<T>) object;
        c.clear();

        final int numberOfEntitiesExpected = listOfKeys.size();
        // does not retain order:
        // List<T> retrievedEntities = p.get().getByKeys(referenceObjClass,
        // (List) __getKeysAsList());

        // so we do it the lousy way: FIXME
        final List<T> retrievedEntities = new ArrayList<T>(listOfKeys.size());
        for (final Key<?> k : listOfKeys) {
            T entity = (T) getDatastore().getByKey(referenceObjClass, k);
            if (entity != null) {
                retrievedEntities.add(entity);
            }
        }

        if (!ignoreMissing && (numberOfEntitiesExpected != retrievedEntities.size())) {
            throw new LazyReferenceFetchingException(format("During the lifetime of a proxy of type '%s', some referenced Entities"
                                                                + " of type '%s' have disappeared from the DataStore.",
                                                            c.getClass().getSimpleName(), referenceObjClass.getSimpleName()));
        }

        c.addAll(retrievedEntities);
        return c;
    }

    private void syncKeys() {
        final DataStore ds = getDatastore();

        listOfKeys.clear();
        for (final Object e : ((Collection) object)) {
            listOfKeys.add(ds.getKey(e));
        }
    }
}
