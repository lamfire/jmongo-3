package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.Datastore;
import com.lamfire.jmongo.Key;

import java.util.Collection;
import java.util.Map;



public interface LazyProxyFactory {

    <T extends Collection> T createListProxy(final Datastore datastore, T listToProxy, Class referenceObjClass, boolean ignoreMissing);


    <T extends Map> T createMapProxy(final Datastore datastore, final T mapToProxy, final Class referenceObjClass,
                                     final boolean ignoreMissing);


    <T> T createProxy(final Datastore datastore, Class<T> targetClass, final Key<T> key, final boolean ignoreMissing);

}
