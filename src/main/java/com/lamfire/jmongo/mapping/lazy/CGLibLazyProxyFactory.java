package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.mapping.lazy.proxy.*;
import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.toys.delegate.DelegationMode;
import com.thoughtworks.proxy.toys.dispatch.Dispatching;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;



@SuppressWarnings("unchecked")
public class CGLibLazyProxyFactory implements LazyProxyFactory {
    private final CglibProxyFactory factory = new CglibProxyFactory();

    @Override
    public <T extends Collection> T createListProxy(final DataStore datastore, final T listToProxy, final Class referenceObjClass,
                                                    final boolean ignoreMissing) {
        final Class<? extends Collection> targetClass = listToProxy.getClass();
        final CollectionObjectReference objectReference = new CollectionObjectReference(listToProxy, referenceObjClass, ignoreMissing,
                                                                                        datastore);

        final T backend = (T) new NonFinalizingHotSwappingInvoker(new Class[]{targetClass, Serializable.class}, factory, objectReference,
                                                                  DelegationMode.SIGNATURE).proxy();

        return (T) Dispatching.proxy(targetClass, new Class[]{ProxiedEntityReferenceList.class, targetClass, Serializable.class})
                              .with(objectReference, backend)
                              .build(factory);

    }

    @Override
    public <T extends Map> T createMapProxy(final DataStore datastore, final T mapToProxy, final Class referenceObjClass,
                                            final boolean ignoreMissing) {
        final Class<? extends Map> targetClass = mapToProxy.getClass();
        final MapObjectReference objectReference = new MapObjectReference(datastore, mapToProxy, referenceObjClass, ignoreMissing);

        final T backend = (T) new NonFinalizingHotSwappingInvoker(new Class[]{targetClass, Serializable.class}, factory, objectReference,
                                                                  DelegationMode.SIGNATURE).proxy();

        return (T) Dispatching.proxy(targetClass, new Class[]{ProxiedEntityReferenceMap.class, targetClass, Serializable.class})
                              .with(objectReference, backend)
                              .build(factory);

    }

    @Override
    public <T> T createProxy(final DataStore datastore, final Class<T> targetClass, final Key<T> key, final boolean ignoreMissing) {

        final EntityObjectReference objectReference = new EntityObjectReference(datastore, targetClass, key, ignoreMissing);

        final T backend = (T) new NonFinalizingHotSwappingInvoker(new Class[]{targetClass, Serializable.class}, factory, objectReference,
                                                                  DelegationMode.SIGNATURE).proxy();

        return (T) Dispatching.proxy(targetClass, new Class[]{ProxiedEntityReference.class, targetClass, Serializable.class})
                              .with(objectReference, backend)
                              .build(factory);

    }
}
