package com.lamfire.jmongo.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MultiLineString implements Geometry {
    private final List<LineString> coordinates;

    @SuppressWarnings("UnusedDeclaration") // needed for Mapping
    private MultiLineString() {
        this.coordinates = new ArrayList<LineString>();
    }

    MultiLineString(final LineString... lineStrings) {
        coordinates = Arrays.asList(lineStrings);
    }

    MultiLineString(final List<LineString> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public List<LineString> getCoordinates() {
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

        MultiLineString that = (MultiLineString) o;

        if (!coordinates.equals(that.coordinates)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "MultiLineString{"
               + "coordinates=" + coordinates
               + '}';
    }
}
