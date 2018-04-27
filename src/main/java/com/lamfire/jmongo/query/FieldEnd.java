package com.lamfire.jmongo.query;


import com.lamfire.jmongo.geo.*;


public interface FieldEnd<T> {


    T contains(String string);


    T containsIgnoreCase(String string);


    T doesNotExist();


    T endsWith(String suffix);


    T endsWithIgnoreCase(String suffix);


    T equal(Object val);


    T equalIgnoreCase(Object val);


    T exists();


    T greaterThan(Object val);


    T greaterThanOrEq(Object val);


    T hasAllOf(Iterable<?> values);


    T hasAnyOf(Iterable<?> values);


    T hasNoneOf(Iterable<?> values);


    @Deprecated
    T hasThisElement(Object val);


    T elemMatch(Query query);


    @Deprecated
    T doesNotHaveThisElement(Object val);


    T hasThisOne(Object val);


    T in(Iterable<?> values);


    T intersects(Geometry geometry);


    T intersects(Geometry geometry, final CoordinateReferenceSystem crs);


    T lessThan(Object val);


    T lessThanOrEq(Object val);


    T mod(long divisor, long remainder);


    T near(double longitude, double latitude);


    T near(double longitude, double latitude, boolean spherical);


    T near(double longitude, double latitude, double radius);


    T near(double longitude, double latitude, double radius, boolean spherical);


    T near(Point point, int maxDistance);


    T near(Point point);


    FieldEnd<T> not();


    T notEqual(Object val);


    T notIn(Iterable<?> values);


    T sizeEq(int val);


    T startsWith(String prefix);


    T startsWithIgnoreCase(final String prefix);


    T type(Type type);


    T within(Shape shape);


    T within(Polygon boundary);


    T within(MultiPolygon boundaries);


    T within(Polygon boundary, CoordinateReferenceSystem crs);


    T within(MultiPolygon boundaries, CoordinateReferenceSystem crs);
}
