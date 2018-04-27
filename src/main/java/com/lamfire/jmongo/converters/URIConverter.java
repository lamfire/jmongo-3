package com.lamfire.jmongo.converters;


import com.lamfire.jmongo.mapping.MappedField;

import java.net.URI;



public class URIConverter extends TypeConverter implements SimpleValueConverter {


    public URIConverter() {
        this(URI.class);
    }

    protected URIConverter(final Class clazz) {
        super(clazz);
    }

    @Override
    public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        return URI.create(val.toString().replace("%46", "."));
    }

    @Override
    public String encode(final Object uri, final MappedField optionalExtraInfo) {
        if (uri == null) {
            return null;
        }

        return uri.toString().replace(".", "%46");
    }
}
