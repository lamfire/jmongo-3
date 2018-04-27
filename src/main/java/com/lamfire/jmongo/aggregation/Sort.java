package com.lamfire.jmongo.aggregation;

@Deprecated
public class Sort extends com.lamfire.jmongo.query.Sort {


    public Sort(final String field, final int direction) {
        super(field, direction);
    }


    @Deprecated
    public static Sort ascending(final String field) {
        return new Sort(field, 1);
    }

    @Deprecated
    public static Sort descending(final String field) {
        return new Sort(field, -1);
    }

    @Deprecated
    public int getDirection() {
        return super.getOrder();
    }
}
