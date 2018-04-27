package com.lamfire.jmongo.query;


import java.util.List;



public interface UpdateOperations<T> {

    @Deprecated
    UpdateOperations<T> add(String field, Object value);


    @Deprecated
    UpdateOperations<T> add(String field, Object value, boolean addDups);


    @Deprecated
    UpdateOperations<T> addAll(String field, List<?> values, boolean addDups);


    UpdateOperations<T> addToSet(String field, Object value);


    UpdateOperations<T> addToSet(String field, List<?> values);


    UpdateOperations<T> dec(String field);


    UpdateOperations<T> dec(String field, Number value);


    UpdateOperations<T> disableValidation();


    UpdateOperations<T> enableValidation();


    UpdateOperations<T> inc(String field);


    UpdateOperations<T> inc(String field, Number value);


    UpdateOperations<T> isolated();


    boolean isIsolated();


    UpdateOperations<T> max(String field, Number value);


    UpdateOperations<T> min(String field, Number value);


    UpdateOperations<T> push(String field, Object value);


    UpdateOperations<T> push(String field, Object value, final PushOptions options);


    UpdateOperations<T> push(String field, List<?> values);


    UpdateOperations<T> push(String field, List<?> values, PushOptions options);


    UpdateOperations<T> removeAll(String field, Object value);


    UpdateOperations<T> removeAll(String field, List<?> values);


    UpdateOperations<T> removeFirst(String field);


    UpdateOperations<T> removeLast(String field);


    UpdateOperations<T> set(String field, Object value);


    UpdateOperations<T> setOnInsert(String field, Object value);


    UpdateOperations<T> unset(String field);
}
