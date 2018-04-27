package com.lamfire.jmongo.geo;

import java.util.List;

interface GeometryFactory {
    Geometry createGeometry(List<?> geometries);
}
