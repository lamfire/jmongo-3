package com.lamfire.jmongo.query;


public class Sort {
    private static final String NATURAL = "$natural";

    private final String field;
    private final int order;


    protected Sort(final String field, final int order) {
        this.field = field;
        this.order = order;
    }


    public static Sort ascending(final String field) {
        return new Sort(field, 1);
    }


    public static Sort descending(final String field) {
        return new Sort(field, -1);
    }


    public static Sort naturalAscending() {
        return new Sort(NATURAL, 1);
    }


    public static Sort naturalDescending() {
        return new Sort(NATURAL, -1);
    }


    public int getOrder() {
        return order;
    }


    public String getField() {
        return field;
    }
}
