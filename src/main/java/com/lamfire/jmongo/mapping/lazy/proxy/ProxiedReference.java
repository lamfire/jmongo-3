package com.lamfire.jmongo.mapping.lazy.proxy;



//CHECKSTYLE:OFF
public interface ProxiedReference {
    Class __getReferenceObjClass();

    boolean __isFetched();

    Object __unwrap();
}
