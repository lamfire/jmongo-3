package com.lamfire.jmongo.query;

import com.lamfire.jmongo.geo.CoordinateReferenceSystem;
import com.lamfire.jmongo.geo.Geometry;
import com.lamfire.jmongo.geo.GeometryQueryConverter;
import com.lamfire.jmongo.geo.NamedCoordinateReferenceSystemConverter;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import static com.lamfire.jmongo.query.FilterOperator.NEAR;


class StandardGeoFieldCriteria extends FieldCriteria {
    private final Integer maxDistanceMeters;
    private final DBObject geometryAsDBObject;
    private CoordinateReferenceSystem crs;

    StandardGeoFieldCriteria(final Query<?> query, final String field, final FilterOperator operator, final Geometry value,
                             final Integer maxDistanceMeters, final CoordinateReferenceSystem crs) {
        this(query, field, operator, value, maxDistanceMeters);
        this.crs = crs;
    }

    @SuppressWarnings("deprecation")
    StandardGeoFieldCriteria(final Query<?> query, final String field, final FilterOperator operator, final Geometry value,
                             final Integer maxDistanceMeters) {
        super(query, field, operator, value);
        this.maxDistanceMeters = maxDistanceMeters;
        GeometryQueryConverter geometryQueryConverter = new GeometryQueryConverter(query.getDatastore().getMapper());
        geometryAsDBObject = (DBObject) geometryQueryConverter.encode(value, null);
    }

    @Override
    public void addTo(final DBObject obj) {
        BasicDBObjectBuilder query;
        FilterOperator operator = getOperator();

        switch (operator) {
            case NEAR:
                if (maxDistanceMeters != null) {
                    geometryAsDBObject.put("$maxDistance", maxDistanceMeters);
                }
                query = BasicDBObjectBuilder.start(NEAR.val(), geometryAsDBObject);
                break;
            case GEO_WITHIN:
            case INTERSECTS:
                query = BasicDBObjectBuilder.start(operator.val(), geometryAsDBObject);
                if (crs != null) {
                    ((DBObject) geometryAsDBObject.get("$geometry")).put("crs", new NamedCoordinateReferenceSystemConverter().encode(crs));
                }
                break;
            default:
                throw new UnsupportedOperationException(String.format("Operator %s not supported for geo-query", operator.val()));
        }

        obj.put(getField(), query.get());
    }
}
