package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;

import java.util.UUID;



public class UUIDConverter extends TypeConverter implements SimpleValueConverter {


    public UUIDConverter() {
        super(UUID.class);
    }

    @Override
    public Object decode(final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
        return fromDBObject == null ? null : UUID.fromString((String) fromDBObject);
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        return value == null ? null : value.toString();
    }
}
