package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;



public class IdentityConverter extends TypeConverter {


    public IdentityConverter(final Class... types) {
        super(types);
    }

    @Override
    public Object decode(final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
        return fromDBObject;
    }

    @Override
    protected boolean isSupported(final Class c, final MappedField optionalExtraInfo) {
        return true;
    }
}
