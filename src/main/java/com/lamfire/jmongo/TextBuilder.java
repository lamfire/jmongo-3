

package com.lamfire.jmongo;

import com.lamfire.jmongo.annotations.IndexOptions;
import com.lamfire.jmongo.annotations.Text;

class TextBuilder extends AnnotationBuilder<Text> implements Text {
    @Override
    public Class<Text> annotationType() {
        return Text.class;
    }

    @Override
    public IndexOptions options() {
        return get("options");
    }

    @Override
    public int value() {
        return get("value");
    }

    TextBuilder options(final IndexOptions options) {
        put("options", options);
        return this;
    }

    TextBuilder value(final int value) {
        put("value", value);
        return this;
    }

}
