package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.utils.ReflectionUtils;

import java.util.List;



public class LongConverter extends TypeConverter implements SimpleValueConverter {


    public LongConverter() {
        super(long.class, Long.class, long[].class, Long[].class);
    }

    @Override
    public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof Long) {
            return val;
        }

        if (val instanceof Number) {
            return ((Number) val).longValue();
        }

        if (val instanceof List) {
            final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
            return ReflectionUtils.convertToArray(type, (List<?>) val);
        }

        return Long.parseLong(val.toString());
    }

}
