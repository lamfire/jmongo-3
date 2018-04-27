package com.lamfire.jmongo.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Polygon implements Geometry {
    private final LineString exteriorBoundary;
    private final List<LineString> interiorBoundaries;

    @SuppressWarnings("UnusedDeclaration") // used by Mapping
    private Polygon() {
        exteriorBoundary = null;
        interiorBoundaries = new ArrayList<LineString>();
    }

    Polygon(final LineString exteriorBoundary, final LineString... interiorBoundaries) {
        this.exteriorBoundary = exteriorBoundary;
        this.interiorBoundaries = Arrays.asList(interiorBoundaries);
    }

    Polygon(final List<LineString> boundaries) {
        exteriorBoundary = boundaries.get(0);
        if (boundaries.size() > 1) {
            interiorBoundaries = boundaries.subList(1, boundaries.size());
        } else {
            interiorBoundaries = new ArrayList<LineString>();
        }
    }

    @Override
    public List<LineString> getCoordinates() {
        List<LineString> polygonBoundaries = new ArrayList<LineString>();
        polygonBoundaries.add(exteriorBoundary);
        polygonBoundaries.addAll(interiorBoundaries);
        return polygonBoundaries;
    }


    public LineString getExteriorBoundary() {
        return exteriorBoundary;
    }


    public List<LineString> getInteriorBoundaries() {
        return Collections.unmodifiableList(interiorBoundaries);
    }

    @Override
    public int hashCode() {
        int result = exteriorBoundary.hashCode();
        result = 31 * result + interiorBoundaries.hashCode();
        return result;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Polygon polygon = (Polygon) o;

        if (!exteriorBoundary.equals(polygon.exteriorBoundary)) {
            return false;
        }
        if (!interiorBoundaries.equals(polygon.interiorBoundaries)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Polygon{"
               + "exteriorBoundary=" + exteriorBoundary
               + ", interiorBoundaries=" + interiorBoundaries
               + '}';
    }
}
