package com.lamfire.jmongo.geo;

import com.lamfire.jmongo.converters.SimpleValueConverter;
import com.lamfire.jmongo.converters.TypeConverter;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.mongodb.BasicDBObject;


public class GeometryQueryConverter extends TypeConverter implements SimpleValueConverter {


    public GeometryQueryConverter(final Mapper mapper) {
        super.setMapper(mapper);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
        throw new UnsupportedOperationException("Should never have to decode a query object");
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        Object encode = getMapper().getConverters().encode(((Geometry) value));
        return new BasicDBObject("$geometry", encode);
    }
}
