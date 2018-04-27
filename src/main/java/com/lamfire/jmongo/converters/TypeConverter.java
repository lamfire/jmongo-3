package com.lamfire.jmongo.converters;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;

import java.util.Arrays;


public abstract class TypeConverter {
    private Mapper mapper;
    private Class[] supportedTypes;

    protected TypeConverter() {
    }

    protected TypeConverter(final Class... types) {
        supportedTypes = copy(types);
    }


    public final Object decode(final Class targetClass, final Object fromDBObject) {
        return decode(targetClass, fromDBObject, null);
    }


    public abstract Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo);


    public final Object encode(final Object value) {
        return encode(value, null);
    }


    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        return value; // as a default impl
    }


    public Mapper getMapper() {
        return mapper;
    }


    public void setMapper(final Mapper mapper) {
        this.mapper = mapper;
    }


    @Deprecated
    public Class[] getSupportTypes() {
        return copy(supportedTypes);
    }


    @Deprecated
    public void setSupportTypes(final Class[] supportTypes) {
        this.supportedTypes = copy(supportTypes);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj != null && getClass().equals(obj.getClass());
    }


    protected boolean isSupported(final Class<?> c, final MappedField optionalExtraInfo) {
        return false;
    }


    protected boolean oneOf(final Class f, final Class... classes) {
        return oneOfClasses(f, classes);
    }


    protected boolean oneOfClasses(final Class f, final Class[] classes) {
        for (final Class c : classes) {
            if (c.equals(f)) {
                return true;
            }
        }
        return false;
    }

    Class[] copy(final Class[] array) {
        return array == null ? null : Arrays.copyOf(array, array.length);
    }


    final Class[] getSupportedTypes() {
        return copy(supportedTypes);
    }


    public void setSupportedTypes(final Class[] supportedTypes) {
        this.supportedTypes = copy(supportedTypes);
    }


    final boolean canHandle(final Class c) {
        return isSupported(c, null);
    }


    final boolean canHandle(final MappedField mf) {
        return isSupported(mf.getType(), mf);
    }
}
