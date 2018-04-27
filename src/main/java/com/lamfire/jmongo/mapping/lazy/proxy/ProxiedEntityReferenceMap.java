package com.lamfire.jmongo.mapping.lazy.proxy;


import com.lamfire.jmongo.Key;

import java.util.Map;



public interface ProxiedEntityReferenceMap extends ProxiedReference {

    //CHECKSTYLE:OFF
    Map<Object, Key<?>> __getReferenceMap();
    //CHECKSTYLE:ON

    //CHECKSTYLE:OFF
    void __put(Object key, Key<?> referenceKey);
    //CHECKSTYLE:ON
}
