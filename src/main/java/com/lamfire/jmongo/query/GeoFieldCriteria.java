package com.lamfire.jmongo.query;


import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

import java.util.Map;


class GeoFieldCriteria extends FieldCriteria {

    private final Map<String, Object> opts;

    GeoFieldCriteria(final Query<?> query, final String field, final FilterOperator op, final Object value,
                     final Map<String, Object> opts) {
        super(query, field, op, value);
        this.opts = opts;
    }

    @Override
    public void addTo(final DBObject obj) {
        final BasicDBObjectBuilder query;
        switch (getOperator()) {
            case NEAR:
                query = BasicDBObjectBuilder.start(FilterOperator.NEAR.val(), getValue());
                break;
            case NEAR_SPHERE:
                query = BasicDBObjectBuilder.start(FilterOperator.NEAR_SPHERE.val(), getValue());
                break;
            case WITHIN_BOX:
                query = BasicDBObjectBuilder.start().push(FilterOperator.GEO_WITHIN.val()).add(getOperator().val(), getValue());
                break;
            case WITHIN_CIRCLE:
                query = BasicDBObjectBuilder.start().push(FilterOperator.GEO_WITHIN.val()).add(getOperator().val(), getValue());
                break;
            case WITHIN_CIRCLE_SPHERE:
                query = BasicDBObjectBuilder.start().push(FilterOperator.GEO_WITHIN.val()).add(getOperator().val(), getValue());
                break;
            default:
                throw new UnsupportedOperationException(getOperator() + " not supported for geo-query");
        }

        //add options...
        if (opts != null) {
            for (final Map.Entry<String, Object> e : opts.entrySet()) {
                query.append(e.getKey(), e.getValue());
            }
        }

        obj.put(getField(), query.get());
    }
}
