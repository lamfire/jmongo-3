

package com.lamfire.jmongo.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class PushOptions {
    private Integer position;
    private Integer slice;
    private Integer sort;
    private DBObject sortDocument;


    public PushOptions() {
    }


    public PushOptions position(final int position) {
        if (position < 0) {
            throw new UpdateException("The position must be at least 0.");
        }
        this.position = position;
        return this;
    }


    public PushOptions slice(final int slice) {
        this.slice = slice;
        return this;
    }


    public PushOptions sort(final int sort) {
        this.sort = sort;
        return this;
    }


    public PushOptions sort(final String field, final int direction) {
        if (sort != null) {
            throw new IllegalStateException("sortDocument can not be set if sort already is");
        }
        if (sortDocument == null) {
            sortDocument = new BasicDBObject();
        }
        sortDocument.put(field, direction);
        return this;
    }

    void update(final BasicDBObject dbObject) {
        if (position != null) {
            dbObject.put("$position", position);
        }
        if (slice != null) {
            dbObject.put("$slice", slice);
        }
        if (sort != null) {
            dbObject.put("$sort", sort);
        }
        if (sortDocument != null) {
            dbObject.put("$sort", sortDocument);
        }
    }


    public static PushOptions options() {
        return new PushOptions();
    }
}
