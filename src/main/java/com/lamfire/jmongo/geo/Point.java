package com.lamfire.jmongo.geo;

import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.annotations.Entity;

import java.util.ArrayList;
import java.util.List;


@Embedded
@Entity(noClassnameStored = true)
public class Point implements Geometry {
    private final List<Double> coordinates = new ArrayList<Double>();

    Point(final double latitude, final double longitude) {
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    Point(final List<Double> coordinates) {
        this.coordinates.addAll(coordinates);
    }

    @Override
    public List<Double> getCoordinates() {
        return coordinates;
    }


    public double getLatitude() {
        return coordinates.get(1);
    }


    public double getLongitude() {
        return coordinates.get(0);
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

        Point point = (Point) o;

        if (getCoordinates().size() != point.getCoordinates().size()) {
            return false;
        }
        for (int i = 0; i < coordinates.size(); i++) {
            final Double coordinate = coordinates.get(i);
            if (Double.compare(coordinate, point.getCoordinates().get(i)) != 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("Point{coordinates=%s}", coordinates);
    }
}
