package com.lamfire.jmongo.mapping.lazy.proxy;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.Key;
import com.thoughtworks.proxy.kit.ObjectReference;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;



@SuppressWarnings({"rawtypes"})
public abstract class AbstractReference implements Serializable, ObjectReference, ProxiedReference {

    private static final long serialVersionUID = 1L;
    //CHECKSTYLE:OFF
    private final DataStore datastore;
    protected final boolean ignoreMissing;
    protected final Class referenceObjClass;
    protected Object object;
    //CHECKSTYLE:ON
    private boolean isFetched;

    protected AbstractReference(final DataStore datastore, final Class referenceObjClass, final boolean ignoreMissing) {
        this.datastore = datastore;
        this.referenceObjClass = referenceObjClass;
        this.ignoreMissing = ignoreMissing;
    }

    //CHECKSTYLE:OFF
    @Override
    public final Class __getReferenceObjClass() {
        //CHECKSTYLE:ON
        return referenceObjClass;
    }

    //CHECKSTYLE:OFF
    @Override
    public final boolean __isFetched() {
        //CHECKSTYLE:ON
        return isFetched;
    }

    //CHECKSTYLE:OFF
    @Override
    public Object __unwrap() {
        //CHECKSTYLE:ON
        return get();
    }

    @Override
    public final synchronized Object get() {
        if (isFetched) {
            return object;
        }

        object = fetch();
        isFetched = true;
        return object;
    }

    @Override
    public final void set(final Object arg0) {
        throw new UnsupportedOperationException();
    }

    protected void beforeWriteObject() {
    }

    @SuppressWarnings("unchecked")
    protected final Object fetch(final Key<?> id) {
        return getDatastore().getByKey(referenceObjClass, id);
    }

    protected abstract Object fetch();

    private void writeObject(final ObjectOutputStream out) throws IOException {
        // excessive hoop-jumping in order not to have to recreate the
        // instance.
        // as soon as weÂ´d have an ObjectFactory, that would be unnecessary
        beforeWriteObject();
        isFetched = false;
        out.defaultWriteObject();
    }


    public DataStore getDatastore() {
        return datastore;
    }
}
