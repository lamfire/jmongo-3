package com.lamfire.jmongo.geo;

import com.lamfire.jmongo.converters.SimpleValueConverter;
import com.lamfire.jmongo.converters.TypeConverter;
import com.lamfire.jmongo.mapping.MappedField;
import com.mongodb.DBObject;


public class GeometryConverter extends TypeConverter implements SimpleValueConverter {

    public GeometryConverter() {
        super(Geometry.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
        DBObject dbObject = (DBObject) fromDBObject;
        String type = (String) dbObject.get("type");
        return getMapper().getConverters().decode(GeoJsonType.fromString(type).getTypeClass(), fromDBObject, optionalExtraInfo);
    }
}
