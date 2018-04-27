

package com.lamfire.jmongo.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Arrays;


public class ArraySlice {
    private Integer limit;
    private Integer skip;


    public ArraySlice(final int limit) {
        this.limit = limit;
    }


    public ArraySlice(final int skip, final int limit) {
        this.skip = skip;
        this.limit = limit;
    }


    public Integer getLimit() {
        return limit;
    }


    public Integer getSkip() {
        return skip;
    }

    DBObject toDatabase() {
        return
            new BasicDBObject("$slice", skip == null ? limit : Arrays.asList(skip, limit));

    }
}
