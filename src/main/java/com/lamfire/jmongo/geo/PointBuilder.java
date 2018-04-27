package com.lamfire.jmongo.geo;


public class PointBuilder {
    private double longitude;
    private double latitude;


    public static PointBuilder pointBuilder() {
        return new PointBuilder();
    }


    public Point build() {
        return new Point(latitude, longitude);
    }


    public PointBuilder latitude(final double latitude) {
        this.latitude = latitude;
        return this;
    }


    public PointBuilder longitude(final double longitude) {
        this.longitude = longitude;
        return this;
    }
}
