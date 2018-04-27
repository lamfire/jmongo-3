

package com.lamfire.jmongo;

import com.lamfire.jmongo.annotations.Field;
import com.lamfire.jmongo.utils.IndexType;

class FieldBuilder extends AnnotationBuilder<Field> implements Field {
    @Override
    public Class<Field> annotationType() {
        return Field.class;
    }

    @Override
    public IndexType type() {
        return get("type");
    }

    @Override
    public String value() {
        return get("value");
    }

    @Override
    public int weight() {
        return get("weight");
    }

    FieldBuilder type(final IndexType type) {
        put("type", type);
        return this;
    }

    FieldBuilder value(final String value) {
        put("value", value);
        return this;
    }

    FieldBuilder weight(final int weight) {
        put("weight", weight);
        return this;
    }

}
