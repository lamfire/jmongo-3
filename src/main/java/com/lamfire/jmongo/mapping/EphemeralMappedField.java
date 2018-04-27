package com.lamfire.jmongo.mapping;

import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.utils.ReflectionUtils;
import com.mongodb.DBObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EphemeralMappedField extends MappedField {
    private ParameterizedType pType;
    private Object value;
    private MappedField parent;


    public EphemeralMappedField(final ParameterizedType t, final MappedField mf, final Mapper mapper) {
        super(mf.getField(), t, mapper);
        parent = mf;
        pType = t;
        final Class rawClass = (Class) t.getRawType();
        setIsSet(ReflectionUtils.implementsInterface(rawClass, Set.class));
        setIsMap(ReflectionUtils.implementsInterface(rawClass, Map.class));
        setMapKeyType(getMapKeyClass());
        setSubType(getSubType());
        setIsMongoType(ReflectionUtils.isPropertyType(getSubClass()));
    }


    public EphemeralMappedField(final Type t, final MappedField mf, final Mapper mapper) {
        super(mf.getField(), t, mapper);
        parent = mf;
    }

    @Override
    public void addAnnotation(final Class<? extends Annotation> clazz) {
    }

    @Override
    public void addAnnotation(final Class<? extends Annotation> clazz, final Annotation ann) {
    }

    @Override
    public Object getDbObjectValue(final DBObject dbObj) {
        return dbObj;
    }

    @Override
    public Object getFieldValue(final Object instance) {
        return value;
    }

    @Override
    public Class getMapKeyClass() {
        return (Class) (isMap() ? pType.getActualTypeArguments()[0] : null);
    }

    @Override
    public String getNameToStore() {
        return "superFake";
    }

    @Override
    public Class getSubClass() {
        return toClass(getSubType());
    }

    @Override
    public Type getSubType() {
        return pType != null ? pType.getActualTypeArguments()[isMap() ? 1 : 0] : null;
    }

    @Override
    public Class getType() {
        if (pType == null) {
            return super.getType();
        } else if (isMap()) {
            return Map.class;
        } else {
            return List.class;
        }
    }

    @Override
    public boolean hasAnnotation(final Class ann) {
        return Embedded.class.equals(ann);
    }

    @Override
    public boolean isSingleValue() {
        return false;
    }

    @Override
    public void setFieldValue(final Object instance, final Object val) {
        value = val;
    }

    @Override
    public String toString() {
        return "EphemeralMappedField for " + super.toString();
    }

    @Override
    protected String getMappedFieldName() {
        return "";
    }


    public MappedField getParent() {
        return parent;
    }


    public Object getValue() {
        return value;
    }
}
