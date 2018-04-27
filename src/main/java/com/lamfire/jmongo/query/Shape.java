package com.lamfire.jmongo.query;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class Shape {
    private final String geometry;
    private final Point[] points;

    Shape(final String geometry, final Point... points) {
        this.geometry = geometry;
        this.points = points;
    }


    public static Shape box(final Point bottomLeft, final Point upperRight) {
        return new Shape("$box", bottomLeft, upperRight);
    }


    public static Shape center(final Point center, final double radius) {
        return new Center("$center", center, radius);
    }


    public static Shape centerSphere(final Point center, final double radius) {
        return new Center("$centerSphere", center, radius);
    }


    public static Shape polygon(final Point... points) {
        return new Shape("$polygon", points);
    }


    public String getGeometry() {
        return geometry;
    }


    public Point[] getPoints() {
        return copy(points);
    }


    public DBObject toDBObject() {
        final BasicDBList list = new BasicDBList();
        for (final Point point : points) {
            list.add(point.toDBObject());
        }

        return new BasicDBObject(geometry, list);
    }

    private Point[] copy(final Point[] array) {
        Point[] copy = new Point[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }


    public static class Point {
        private final double longitude;
        private final double latitude;


        public Point(final double longitude, final double latitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }


        public DBObject toDBObject() {
            final BasicDBList list = new BasicDBList();
            list.add(longitude);
            list.add(latitude);
            return list;
        }
    }

    private static class Center extends Shape {
        private final Point center;
        private final double radius;

        public Center(final String geometry, final Point center, final double radius) {
            super(geometry);
            this.center = center;
            this.radius = radius;
        }

        @Override
        public DBObject toDBObject() {
            final BasicDBList list = new BasicDBList();
            list.add(center.toDBObject());
            list.add(radius);

            return new BasicDBObject(this.getGeometry(), list);
        }
    }
}
