package com.lamfire.jmongo.query;


import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import org.bson.types.CodeWScope;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public interface Query<T> extends QueryResults<T>, Cloneable {

    CriteriaContainer and(Criteria... criteria);


    Query<T> batchSize(int value);


    Query<T> cloneQuery();

    Query<T> comment(String comment);

    FieldEnd<? extends CriteriaContainerImpl> criteria(String field);

    Query<T> disableCursorTimeout();

    Query<T> disableSnapshotMode();

    Query<T> disableValidation();

    Query<T> enableCursorTimeout();

    Query<T> enableSnapshotMode();

    Query<T> enableValidation();

    Map<String, Object> explain();

    Map<String, Object> explain(FindOptions options);

    FieldEnd<? extends Query<T>> field(String field);

    Query<T> filter(String condition, Object value);

    int getBatchSize();

    DBCollection getCollection();

    Class<T> getEntityClass();

    DBObject getFieldsObject();

    int getLimit();

    int getOffset();

    DBObject getQueryObject();

    DBObject getSortObject();

    Query<T> hintIndex(String idxName);

    Query<T> limit(int value);

    Query<T> lowerIndexBound(DBObject lowerBound);

    Query<T> maxScan(int value);

    Query<T> maxTime(long maxTime, TimeUnit maxTimeUnit);

    Query<T> offset(int value);

    CriteriaContainer or(Criteria... criteria);

    Query<T> order(String sort);

    Query<T> order(Meta sort);

    Query<T> order(Sort... sorts);

    Query<T> project(String field, boolean include);

    Query<T> project(String field, ArraySlice slice);

    Query<T> project(Meta meta);

    Query<T> queryNonPrimary();

    Query<T> queryPrimaryOnly();

    Query<T> retrieveKnownFields();

    Query<T> retrievedFields(boolean include, String... fields);

    Query<T> returnKey();

    Query<T> search(String text);

    Query<T> search(String text, String language);

    Query<T> upperIndexBound(DBObject upperBound);

    Query<T> useReadPreference(ReadPreference readPref);

    Query<T> where(String js);

    Query<T> where(CodeWScope js);

    Query<T> includeFieldsOnly(String... fields);

    Query<T> excludeFields(String... fields);
}
