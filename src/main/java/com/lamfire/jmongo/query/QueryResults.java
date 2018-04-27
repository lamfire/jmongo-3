package com.lamfire.jmongo.query;


import com.lamfire.jmongo.Key;

import java.util.List;


public interface QueryResults<T> extends Iterable<T> {

    List<Key<T>> asKeyList();


    List<T> asList();


    List<Key<T>> asKeyList(FindOptions options);


    List<T> asList(FindOptions options);


    @Deprecated
    long countAll();


    long count();


    long count(CountOptions options);


    QueryIterator<T, T> fetch();


    QueryIterator<T, T> fetch(FindOptions options);


    QueryIterator<T, T> fetchEmptyEntities();

    QueryIterator<T, T> fetchEmptyEntities(FindOptions options);


    QueryKeyIterator<T> fetchKeys();

    QueryKeyIterator<T> fetchKeys(FindOptions options);


    T get();


    T get(FindOptions options);


    Key<T> getKey();


    Key<T> getKey(FindOptions options);


    @Deprecated
    QueryIterator<T, T> tail();


    @Deprecated
    QueryIterator<T, T> tail(boolean awaitData);
}
