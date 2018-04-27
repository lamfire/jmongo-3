

package com.lamfire.jmongo;

import com.lamfire.jmongo.annotations.IndexOptions;
import com.lamfire.jmongo.annotations.Indexed;
import com.lamfire.jmongo.utils.IndexDirection;

@SuppressWarnings("deprecation")
class IndexedBuilder extends AnnotationBuilder<Indexed> implements Indexed {
    @Override
    public Class<Indexed> annotationType() {
        return Indexed.class;
    }

    @Override
    public IndexOptions options() {
        return get("options");
    }

    @Override
    public boolean background() {
        return get("background");
    }

    @Override
    public boolean dropDups() {
        return get("dropDups");
    }

    @Override
    public int expireAfterSeconds() {
        return get("expireAfterSeconds");
    }

    @Override
    public String name() {
        return get("name");
    }

    @Override
    public boolean sparse() {
        return get("sparse");
    }

    @Override
    public boolean unique() {
        return get("unique");
    }

    @Override
    public IndexDirection value() {
        return get("value");
    }

    IndexedBuilder options(final IndexOptions options) {
        put("options", options);
        return this;
    }

    IndexedBuilder background(final boolean background) {
        put("background", background);
        return this;
    }

    IndexedBuilder dropDups(final boolean dropDups) {
        put("dropDups", dropDups);
        return this;
    }

    IndexedBuilder expireAfterSeconds(final int expireAfterSeconds) {
        put("expireAfterSeconds", expireAfterSeconds);
        return this;
    }

    IndexedBuilder name(final String name) {
        put("name", name);
        return this;
    }

    IndexedBuilder sparse(final boolean sparse) {
        put("sparse", sparse);
        return this;
    }

    IndexedBuilder unique(final boolean unique) {
        put("unique", unique);
        return this;
    }

    IndexedBuilder value(final IndexDirection value) {
        put("value", value);
        return this;
    }

}
