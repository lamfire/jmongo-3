package com.lamfire.jmongo.aggregation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public final class Group {
    private final String name;
    private Group nested;
    private List<Projection> projections;
    private Accumulator accumulator;
    private String sourceField;

    private Group(final String name, final Accumulator accumulator) {
        this.name = name;
        this.accumulator = accumulator;
    }

    private Group(final String name, final String sourceField) {
        this.name = name;
        this.sourceField = "$" + sourceField;
    }

    private Group(final String name, final Projection... projections) {
        this.name = name;
        this.projections = asList(projections);
    }

    private Group(final String name, final Group nested) {
        this.name = name;
        this.nested = nested;
    }

    public static List<Group> id(final Group... fields) {
        return asList(fields);
    }

    public static Group grouping(final String name) {
        return grouping(name, name);
    }


    public static Group grouping(final String name, final Projection... projections) {
        return new Group(name, projections);
    }

    public static Group grouping(final String name, final Group group) {
        return new Group(name, group);
    }

    public static Group grouping(final String name, final String sourceField) {
        return new Group(name, sourceField);
    }


    public static Group grouping(final String name, final Accumulator accumulator) {
        return new Group(name, accumulator);
    }

    public static Accumulator addToSet(final String field) {
        return new Accumulator("$addToSet", field);
    }

    
    public static Accumulator average(final String field) {
        return new Accumulator("$avg", field);
    }

    
    public static Accumulator first(final String field) {
        return new Accumulator("$first", field);
    }

    
    public static Accumulator last(final String field) {
        return new Accumulator("$last", field);
    }

    
    public static Accumulator max(final String field) {
        return new Accumulator("$max", field);
    }

    
    public static Accumulator min(final String field) {
        return new Accumulator("$min", field);
    }

    
    public static Accumulator push(final String field) {
        return new Accumulator("$push", field);
    }

    
    public static Accumulator sum(final String field) {
        return new Accumulator("$sum", field);
    }

    
    public Accumulator getAccumulator() {
        return accumulator;
    }

    
    public String getName() {
        return name;
    }

    
    public String getSourceField() {
        return sourceField;
    }

    
    public List<Projection> getProjections() {
        return projections != null ? new ArrayList<Projection>(projections) : null;
    }

    
    public Group getNested() {
        return nested;
    }
}
