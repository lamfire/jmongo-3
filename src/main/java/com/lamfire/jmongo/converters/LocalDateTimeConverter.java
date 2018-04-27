

package com.lamfire.jmongo.converters;

import com.lamfire.jmongo.mapping.MappedField;

import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneId.systemDefault;


@SuppressWarnings("Since15")
public class LocalDateTimeConverter extends TypeConverter implements SimpleValueConverter {


    public LocalDateTimeConverter() {
        super(LocalDateTime.class);
    }

    @Override
    public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof LocalDateTime) {
            return val;
        }

        if (val instanceof Date) {
            return LocalDateTime.ofInstant(((Date) val).toInstant(), systemDefault());
        }

        throw new IllegalArgumentException("Can't convert to LocalDateTime from " + val);
    }

    @Override
    public Object encode(final Object value, final MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }
        return Date.from(((LocalDateTime) value).atZone(systemDefault()).toInstant());
    }
}
