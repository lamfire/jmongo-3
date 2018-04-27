package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.annotations.Serialized;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.MappingException;
import com.lamfire.jmongo.mapping.Serializer;
import org.bson.types.Binary;

import java.io.IOException;

import static java.lang.String.format;



public class SerializedObjectConverter extends TypeConverter {
    @Override
    public Object decode(final Class targetClass, final Object fromDBObject, final MappedField f) {
        if (fromDBObject == null) {
            return null;
        }

        if (!((fromDBObject instanceof Binary) || (fromDBObject instanceof byte[]))) {
            throw new MappingException(format("The stored data is not a DBBinary or byte[] instance for %s ; it is a %s",
                                              f.getFullName(), fromDBObject.getClass().getName()));
        }

        try {
            final boolean useCompression = !f.getAnnotation(Serialized.class).disableCompression();
            return Serializer.deserialize(fromDBObject, useCompression);
        } catch (IOException e) {
            throw new MappingException("While deserializing to " + f.getFullName(), e);
        } catch (ClassNotFoundException e) {
            throw new MappingException("While deserializing to " + f.getFullName(), e);
        }
    }

    @Override
    public Object encode(final Object value, final MappedField f) {
        if (value == null) {
            return null;
        }
        try {
            final boolean useCompression = !f.getAnnotation(Serialized.class).disableCompression();
            return Serializer.serialize(value, useCompression);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected boolean isSupported(final Class c, final MappedField optionalExtraInfo) {
        return optionalExtraInfo != null && (optionalExtraInfo.hasAnnotation(Serialized.class));
    }

}
