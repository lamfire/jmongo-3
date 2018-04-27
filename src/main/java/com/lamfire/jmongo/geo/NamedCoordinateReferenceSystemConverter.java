package com.lamfire.jmongo.geo;

import com.lamfire.jmongo.converters.SimpleValueConverter;
import com.lamfire.jmongo.converters.TypeConverter;
import com.lamfire.jmongo.mapping.MappedField;
import com.mongodb.BasicDBObject;


public class NamedCoordinateReferenceSystemConverter extends TypeConverter implements SimpleValueConverter {

    public NamedCoordinateReferenceSystemConverter() {
        super(NamedCoordinateReferenceSystem.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
        throw new UnsupportedOperationException("We should never need to decode these");
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        NamedCoordinateReferenceSystem crs = (NamedCoordinateReferenceSystem) value;
        final BasicDBObject dbObject = new BasicDBObject("type", crs.getType().getTypeName());
        dbObject.put("properties", new BasicDBObject("name", crs.getName()));

        return dbObject;
    }

    @Override
    protected boolean isSupported(final Class<?> c, final MappedField optionalExtraInfo) {
        return CoordinateReferenceSystem.class.isAssignableFrom(c);
    }
}
