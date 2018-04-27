package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.utils.ReflectionUtils;

import java.util.List;



public class DoubleConverter extends TypeConverter implements SimpleValueConverter {


    public DoubleConverter() {
        super(double.class, Double.class, double[].class, Double[].class);
    }

    @Override
    public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        if (val instanceof Double) {
            return val;
        }

        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }

        if (val instanceof List) {
            final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
            return ReflectionUtils.convertToArray(type, (List<?>) val);
        }

        return Double.parseDouble(val.toString());
    }
}
