package com.lamfire.jmongo.mapping.validation;


import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;



public class ConstraintViolation {
    private final MappedClass clazz;
    private final Class<? extends ClassConstraint> validator;
    private final String message;
    private final Level level;
    private MappedField field;


    public ConstraintViolation(final Level level, final MappedClass clazz, final MappedField field,
                               final Class<? extends ClassConstraint> validator, final String message) {
        this(level, clazz, validator, message);
        this.field = field;
    }


    public ConstraintViolation(final Level level, final MappedClass clazz, final Class<? extends ClassConstraint> validator,
                               final String message) {
        this.level = level;
        this.clazz = clazz;
        this.message = message;
        this.validator = validator;
    }


    public Level getLevel() {
        return level;
    }


    public String getPrefix() {
        final String fn = (field != null) ? field.getJavaFieldName() : "";
        return clazz.getClazz().getName() + "." + fn;
    }


    public String render() {
        return String.format("%s complained about %s : %s", validator.getSimpleName(), getPrefix(), message);
    }


    public enum Level {
        MINOR,
        INFO,
        WARNING,
        SEVERE,
        FATAL
    }
}
