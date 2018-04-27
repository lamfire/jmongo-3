package com.lamfire.jmongo.aggregation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class  Projection {

    private final String target;
    private final String source;
    private List<Projection> projections;
    private List<Object> arguments;
    private boolean suppressed = false;

    private Projection(final String field, final String source) {
        this.target = field;
        this.source = "$" + source;
    }

    private Projection(final String field, final Projection projection, final Projection... subsequent) {
        this(field);
        this.projections = new ArrayList<Projection>();
        projections.add(projection);
        projections.addAll(Arrays.asList(subsequent));
    }

    private Projection(final String field) {
        this.target = field;
        source = null;
    }

    private Projection(final String expression, final Object... args) {
        this(expression);
        this.arguments = Arrays.asList(args);
    }


    public static  Projection projection(final String field) {
        return new Projection(field);
    }


    public static  Projection projection(final String field, final String projectedField) {
        return new Projection(field, projectedField);
    }


    public static  Projection projection(final String field, final Projection projection, final Projection... subsequent) {
        return new Projection(field, projection, subsequent);
    }


    public static  Projection expression(final String operator, final Object... args) {
        return new Projection(operator, args);
    }


    public static  Projection list(final Object... args) {
        return new Projection(null, args);
    }


    public static  Projection add(final Object... args) {
        return expression("$add", args);
    }


    public static  Projection subtract(final Object arg1, final Object arg2) {
        return expression("$subtract", arg1, arg2);
    }


    public static  Projection multiply(final Object... args) {
        return expression("$multiply", args);
    }


    public static  Projection divide(final Object arg1, final Object arg2) {
        return expression("$divide", arg1, arg2);
    }


    public static  Projection mod(final Object arg1, final Object arg2) {
        return expression("$mod", arg1, arg2);
    }


    public List<Object> getArguments() {
        return arguments;
    }


    public String getSource() {
        return source;
    }


    public List<Projection> getProjections() {
        return projections;
    }


    public String getTarget() {
        return target;
    }


    public boolean isSuppressed() {
        return suppressed;
    }


    public Projection suppress() {
        suppressed = true;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Projection{projectedField='%s', sourceField='%s', projections=%s, suppressed=%s}",
                             source, target, projections, suppressed);
    }
}
