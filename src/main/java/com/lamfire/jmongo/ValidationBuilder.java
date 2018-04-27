

package com.lamfire.jmongo;

import com.lamfire.jmongo.annotations.Validation;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;


public class ValidationBuilder extends AnnotationBuilder<Validation> implements Validation {

    public ValidationBuilder action(final ValidationAction action) {
        put("action", action);
        return this;
    }

    @Override
    public Class<Validation> annotationType() {
        return Validation.class;
    }


    public ValidationBuilder level(final ValidationLevel level) {
        put("level", level);
        return this;
    }

    @Override
    public String value() {
        return get("value");
    }

    @Override
    public ValidationLevel level() {
        return get("level");
    }

    @Override
    public ValidationAction action() {
        return get("action");
    }


    public ValidationBuilder value(final String value) {
        put("value", value);
        return this;
    }
}
