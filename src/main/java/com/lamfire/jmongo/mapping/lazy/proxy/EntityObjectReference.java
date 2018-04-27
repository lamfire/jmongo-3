package com.lamfire.jmongo.mapping.lazy.proxy;


import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.Key;

import static java.lang.String.format;



public class EntityObjectReference extends AbstractReference implements ProxiedEntityReference {
    private static final long serialVersionUID = 1L;
    private final Key key;


    public EntityObjectReference(final Datastore datastore, final Class targetClass, final Key key, final boolean ignoreMissing) {
        super(datastore, targetClass, ignoreMissing);
        this.key = key;
    }

    //CHECKSTYLE:OFF
    @Override
    public Key __getKey() {
        return key;
    }
    //CHECKSTYLE:ON

    @Override
    protected void beforeWriteObject() {
        object = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object fetch() {
        final Object entity = getDatastore().getByKey(referenceObjClass, key);
        if (entity == null && !ignoreMissing) {
            throw new LazyReferenceFetchingException(format("During the lifetime of the proxy, the Entity identified by '%s' "
                                                                + "disappeared from the Datastore.", key));
        }
        return entity;
    }
}
