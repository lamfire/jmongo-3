

package com.lamfire.jmongo.converters;

import com.lamfire.jmongo.mapping.MappedField;

import java.time.Instant;
import java.util.Date;


@SuppressWarnings("Since15")
public class InstantConverter extends TypeConverter implements SimpleValueConverter {


    public InstantConverter() {
        super(Instant.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof Instant) {
            return val;
        }

        if (val instanceof Date) {
            return ((Date) val).toInstant();
        }

        throw new IllegalArgumentException("Can't convert to Instant from " + val);
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }
        return Date.from((Instant) value);
    }
}
