package com.lamfire.jmongo.geo;

import java.util.List;


@SuppressWarnings("unchecked") // can't know, or define generics for, the Lists in the factory
public enum GeoJsonType implements GeometryFactory {
    POINT("Point", Point.class) {
        @Override
        public Geometry createGeometry(final List coordinates) {
            return new Point(coordinates);
        }
    },
    LINE_STRING("LineString", LineString.class) {
        @Override
        public Geometry createGeometry(final List objects) {
            return new LineString(objects);
        }
    },
    POLYGON("Polygon", Polygon.class) {
        @Override
        public Geometry createGeometry(final List boundaries) {
            return new Polygon(boundaries);
        }
    },
    MULTI_POINT("MultiPoint", MultiPoint.class) {
        @Override
        public Geometry createGeometry(final List points) {
            return new MultiPoint(points);
        }
    },
    MULTI_LINE_STRING("MultiLineString", MultiLineString.class) {
        @Override
        public Geometry createGeometry(final List lineStrings) {
            return new MultiLineString(lineStrings);
        }
    },
    MULTI_POLYGON("MultiPolygon", MultiPolygon.class) {
        @Override
        public Geometry createGeometry(final List polygons) {
            return new MultiPolygon(polygons);
        }
    };

    private final String type;
    private final Class<? extends Geometry> typeClass;

    GeoJsonType(final String type, final Class<? extends Geometry> typeClass) {
        this.type = type;
        this.typeClass = typeClass;
    }


    public static GeoJsonType fromString(final String type) {
        if (type != null) {
            for (final GeoJsonType geoJsonType : values()) {
                if (type.equalsIgnoreCase(geoJsonType.getType())) {
                    return geoJsonType;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Cannot decode type into GeoJsonType. Type= '%s'", type));
    }


    public String getType() {
        return type;
    }


    public Class<? extends Geometry> getTypeClass() {
        return typeClass;
    }
}
