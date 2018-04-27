

package com.lamfire.jmongo.converters;

import com.lamfire.jmongo.mapping.MappedField;

import java.time.LocalTime;


@SuppressWarnings("Since15")
public class LocalTimeConverter extends TypeConverter implements SimpleValueConverter {

    private static final int MILLI_MODULO = 1000000;


    public LocalTimeConverter() {
        super(LocalTime.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof LocalTime) {
            return val;
        }

        if (val instanceof Number) {
            return LocalTime.ofNanoOfDay(((Number) val).longValue() * MILLI_MODULO);
        }

        throw new IllegalArgumentException("Can't convert to LocalTime from " + val);
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }
        LocalTime time = (LocalTime) value;

        return time.toNanoOfDay() / MILLI_MODULO;
    }
}
