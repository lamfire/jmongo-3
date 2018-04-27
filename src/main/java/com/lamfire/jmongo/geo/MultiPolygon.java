package com.lamfire.jmongo.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MultiPolygon implements Geometry {
    private final List<Polygon> coordinates;

    @SuppressWarnings("UnusedDeclaration") // used by Mapping
    private MultiPolygon() {
        coordinates = new ArrayList<Polygon>();
    }

    MultiPolygon(final Polygon... polygons) {
        coordinates = Arrays.asList(polygons);
    }

    MultiPolygon(final List<Polygon> polygons) {
        coordinates = polygons;
    }

    @Override
    public List<Polygon> getCoordinates() {
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

        MultiPolygon that = (MultiPolygon) o;

        if (!coordinates.equals(that.coordinates)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "MultiPolygon{"
               + "coordinates=" + coordinates
               + '}';
    }
}
