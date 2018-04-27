package com.lamfire.jmongo.aggregation;

import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.Sort;
import com.mongodb.AggregationOptions;
import com.mongodb.ReadPreference;

import java.util.Iterator;
import java.util.List;

public interface AggregationPipeline {

    <U> Iterator<U> aggregate(Class<U> target);


    <U> Iterator<U> aggregate(Class<U> target, AggregationOptions options);


    <U> Iterator<U> aggregate(Class<U> target, AggregationOptions options, ReadPreference readPreference);


    <U> Iterator<U> aggregate(String collectionName, Class<U> target, AggregationOptions options, ReadPreference readPreference);


    AggregationPipeline geoNear(GeoNear geoNear);


    AggregationPipeline group(Group... groupings);


    AggregationPipeline group(String id, Group... groupings);


    AggregationPipeline group(List<Group> id, Group... groupings);


    AggregationPipeline limit(int count);


    AggregationPipeline lookup(String from, String localField, String foreignField, String as);


    AggregationPipeline match(Query query);


    <U> Iterator<U> out(Class<U> target);


    <U> Iterator<U> out(Class<U> target, AggregationOptions options);


    <U> Iterator<U> out(String collectionName, Class<U> target);


    <U> Iterator<U> out(String collectionName, Class<U> target, AggregationOptions options);


    AggregationPipeline project(Projection... projections);


    AggregationPipeline skip(int count);


    AggregationPipeline sort(Sort... sorts);


    AggregationPipeline unwind(String field);
}
