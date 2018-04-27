package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;
import org.bson.types.ObjectId;



public class ObjectIdConverter extends TypeConverter implements SimpleValueConverter {


    public ObjectIdConverter() {
        super(ObjectId.class);
    }

    @Override
    public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof ObjectId) {
            return val;
        }

        return new ObjectId(val.toString());
    }
}
