package com.lamfire.jmongo.mapping.lazy.proxy;


import com.lamfire.jmongo.Key;

import java.util.Collection;
import java.util.List;



public interface ProxiedEntityReferenceList extends ProxiedReference {
    //CHECKSTYLE:OFF
    void __add(Key<?> key);

    void __addAll(Collection<? extends Key<?>> keys);

    List<Key<?>> __getKeysAsList();
    //CHECKSTYLE:ON
}
