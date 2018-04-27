package com.lamfire.jmongo.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LineString implements Geometry {
    private final List<Point> coordinates;

    @SuppressWarnings("UnusedDeclaration") // used by Mapping
    private LineString() {
        coordinates = new ArrayList<Point>();
    }

    LineString(final Point... points) {
        this.coordinates = Arrays.asList(points);
    }

    LineString(final List<Point> points) {
        coordinates = points;
    }

    @Override
    public List<Point> getCoordinates() {
        return coordinates;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LineString that = (LineString) o;

        if (!coordinates.equals(that.coordinates)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "LineString{"
               + "coordinates=" + coordinates
               + '}';
    }
}
