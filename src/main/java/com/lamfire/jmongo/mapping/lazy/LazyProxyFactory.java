package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.DataStore;
import com.lamfire.jmongo.Key;

import java.util.Collection;
import java.util.Map;



public interface LazyProxyFactory {

    <T extends Collection> T createListProxy(final DataStore datastore, T listToProxy, Class referenceObjClass, boolean ignoreMissing);


    <T extends Map> T createMapProxy(final DataStore datastore, final T mapToProxy, final Class referenceObjClass,
                                     final boolean ignoreMissing);


    <T> T createProxy(final DataStore datastore, Class<T> targetClass, final Key<T> key, final boolean ignoreMissing);

}
