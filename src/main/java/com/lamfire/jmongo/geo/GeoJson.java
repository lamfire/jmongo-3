package com.lamfire.jmongo.geo;


public final class GeoJson {
    private GeoJson() {
    }


    public static Point point(final double latitude, final double longitude) {
        return new Point(latitude, longitude);
    }


    public static Polygon polygon(final Point... points) {
        LineString exteriorBoundary = lineString(points);
        ensurePolygonIsClosed(exteriorBoundary);
        return new Polygon(exteriorBoundary);
    }


    public static LineString lineString(final Point... points) {
        return new LineString(points);
    }

    private static void ensurePolygonIsClosed(final LineString points) {
        int size = points.getCoordinates().size();
        if (size > 0 && !points.getCoordinates().get(0).equals(points.getCoordinates().get(size - 1))) {
            throw new IllegalArgumentException("A polygon requires the starting point to be the same as the end to ensure a closed "
                                               + "area");
        }
    }


    public static Polygon polygon(final LineString exteriorBoundary, final LineString... interiorBoundaries) {
        ensurePolygonIsClosed(exteriorBoundary);
        for (final LineString boundary : interiorBoundaries) {
            ensurePolygonIsClosed(boundary);
        }
        return new Polygon(exteriorBoundary, interiorBoundaries);
    }


    public static MultiPoint multiPoint(final Point... points) {
        return new MultiPoint(points);
    }


    public static MultiLineString multiLineString(final LineString... lines) {
        return new MultiLineString(lines);
    }


    public static MultiPolygon multiPolygon(final Polygon... polygons) {
        return new MultiPolygon(polygons);
    }


    public static GeometryCollection geometryCollection(final Geometry... geometries) {
        return new GeometryCollection(geometries);
    }
}
