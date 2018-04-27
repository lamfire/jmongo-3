package com.lamfire.jmongo.aggregation;

import com.lamfire.jmongo.geo.Geometry;
import com.lamfire.jmongo.geo.GeometryShapeConverter;
import com.lamfire.jmongo.geo.Point;
import com.lamfire.jmongo.query.Query;

public final class GeoNear {
    private final double[] nearLegacy;
    private final Geometry nearGeoJson;
    private final String distanceField;
    private final Long limit;
    private final Long maxDocuments;
    private final Double maxDistance;
    private final Query query;
    private final Boolean spherical;
    private final Double distanceMultiplier;
    private final String includeLocations;
    private final Boolean uniqueDocuments;

    private GeoNear(final GeoNearBuilder builder) {
        nearLegacy = builder.nearLegacy;
        nearGeoJson = builder.nearGeoJson;
        distanceField = builder.distanceField;
        limit = builder.limit;
        maxDocuments = builder.maxDocuments;
        maxDistance = builder.maxDistance;
        query = builder.query;
        spherical = builder.spherical;
        distanceMultiplier = builder.distanceMultiplier;
        includeLocations = builder.includeLocations;
        uniqueDocuments = builder.uniqueDocuments;
    }


    public static GeoNearBuilder builder(final String distanceField) {
        return new GeoNearBuilder(distanceField);
    }


    public String getDistanceField() {
        return distanceField;
    }


    public Double getDistanceMultiplier() {
        return distanceMultiplier;
    }


    public String getIncludeLocations() {
        return includeLocations;
    }


    public Long getLimit() {
        return limit;
    }


    public Double getMaxDistance() {
        return maxDistance;
    }


    public Long getMaxDocuments() {
        return maxDocuments;
    }


    public double[] getNear() {
        double[] copy = new double[0];
        if (nearLegacy != null) {
            copy = new double[nearLegacy.length];
            System.arraycopy(nearLegacy, 0, copy, 0, nearLegacy.length);
        }
        return copy;
    }

    Object getNearAsDBObject(final GeometryShapeConverter.PointConverter pointConverter) {
        if (nearGeoJson != null) {
            return pointConverter.encode(nearGeoJson);
        } else {
            return getNear();
        }
    }


    public Query getQuery() {
        return query;
    }


    public Boolean getSpherical() {
        return spherical;
    }


    @Deprecated
    public Boolean getUniqueDocuments() {
        return uniqueDocuments;
    }


    public static class GeoNearBuilder {
        private final String distanceField;
        private Long limit;
        private Long maxDocuments;
        private Double maxDistance;
        private Query query;
        private Boolean spherical;
        private Double distanceMultiplier;
        private String includeLocations;
        private Boolean uniqueDocuments;
        private double[] nearLegacy;
        private Geometry nearGeoJson;


        public GeoNearBuilder(final String distanceField) {
            this.distanceField = distanceField;
        }


        public GeoNear build() {
            return new GeoNear(this);
        }


        public GeoNearBuilder setDistanceMultiplier(final Double distanceMultiplier) {
            this.distanceMultiplier = distanceMultiplier;
            return this;
        }


        public GeoNearBuilder setIncludeLocations(final String includeLocations) {
            this.includeLocations = includeLocations;
            return this;
        }


        public GeoNearBuilder setLimit(final Long limit) {
            this.limit = limit;
            return this;
        }


        public GeoNearBuilder setMaxDistance(final Double maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }


        public GeoNearBuilder setMaxDocuments(final Long num) {
            this.maxDocuments = num;
            return this;
        }


        public GeoNearBuilder setNear(final double latitude, final double longitude) {
            this.nearLegacy = new double[]{longitude, latitude};
            return this;
        }


        public GeoNearBuilder setNear(final Point point) {
            this.nearGeoJson = point;
            return this;
        }


        public GeoNearBuilder setQuery(final Query query) {
            this.query = query;
            return this;
        }


        public GeoNearBuilder setSpherical(final Boolean spherical) {
            this.spherical = spherical;
            return this;
        }


        @Deprecated
        public GeoNearBuilder setUniqueDocuments(final Boolean uniqueDocuments) {
            this.uniqueDocuments = uniqueDocuments;
            return this;
        }
    }
}
