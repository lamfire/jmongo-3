

package com.lamfire.jmongo.geo;

import static java.lang.String.format;


public final class NamedCoordinateReferenceSystem extends CoordinateReferenceSystem {


    public static final NamedCoordinateReferenceSystem EPSG_4326 =
        new NamedCoordinateReferenceSystem("EPSG:4326");


    public static final NamedCoordinateReferenceSystem CRS_84 =
        new NamedCoordinateReferenceSystem("urn:ogc:def:crs:OGC:1.3:CRS84");


    public static final NamedCoordinateReferenceSystem EPSG_4326_STRICT_WINDING =
        new NamedCoordinateReferenceSystem("urn:x-mongodb:crs:strictwinding:EPSG:4326");

    private final String name;


    private NamedCoordinateReferenceSystem(final String name) {
        this.name = name;

    }


    public String getName() {
        return name;
    }

    @Override
    public CoordinateReferenceSystemType getType() {
        return CoordinateReferenceSystemType.NAME;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NamedCoordinateReferenceSystem that = (NamedCoordinateReferenceSystem) o;

        return name.equals(that.name);

    }

    @Override
    public String toString() {
        return format("NamedCoordinateReferenceSystem{name='%s'}", name);
    }
}
