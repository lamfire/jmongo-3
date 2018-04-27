package com.lamfire.jmongo.aggregation;

import com.mongodb.DBObject;

interface AggregationElement {

    DBObject toDBObject();
}
