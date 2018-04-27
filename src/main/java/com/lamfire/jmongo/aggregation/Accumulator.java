package com.lamfire.jmongo.aggregation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;


public class Accumulator implements AggregationElement {
    private final String operation;
    private final Object value;


    public Accumulator(final String operation, final String field) {
        this(operation, (Object) ("$" + field));
    }


    public Accumulator(final String operation, final Object field) {
        this.operation = operation;
        this.value = field;
    }


    public static Accumulator accumulator(final String operation, final String field) {
        return new Accumulator(operation, field);
    }


    public static Accumulator accumulator(final String operation, final Object field) {
        return new Accumulator(operation, field);
    }


    @Deprecated
    public Object getField() {
        return getValue();
    }


    public String getOperation() {
        return operation;
    }


    public Object getValue() {
        return value;
    }

    @Override
    public DBObject toDBObject() {
        BasicDBObject dbObject = new BasicDBObject();
        if (value instanceof List) {
            List<Object> dbValue = new ArrayList<Object>();
            for (Object o : (List) value) {
                if (o instanceof AggregationElement) {
                    dbValue.add(((AggregationElement) o).toDBObject());
                } else {
                    dbValue.add(o);
                }
            }
            dbObject.put(operation, dbValue);
        } else if (value instanceof AggregationElement) {
            dbObject.put(operation, ((AggregationElement) value).toDBObject());
        } else {
            dbObject.put(operation, value);
        }

        return dbObject;
    }
}
