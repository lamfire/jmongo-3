

package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.annotations.*;
import com.lamfire.jmongo.logging.JmongoLoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.utils.ReflectionUtils;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;



@SuppressWarnings("unchecked")
public class MappedField {
    private static final Logger LOG = JmongoLoggerFactory.get(MappedField.class);
    // The Annotations to look for when reflecting on the field (stored in the mappingAnnotations)
    private static final List<Class<? extends Annotation>> INTERESTING = new ArrayList<Class<? extends Annotation>>();

    static {
        INTERESTING.add(Serialized.class);
        INTERESTING.add(Indexed.class);
        INTERESTING.add(Property.class);
        INTERESTING.add(Reference.class);
        INTERESTING.add(Embedded.class);
        INTERESTING.add(Id.class);
        INTERESTING.add(Version.class);
        INTERESTING.add(ConstructorArgs.class);
        INTERESTING.add(AlsoLoad.class);
        INTERESTING.add(NotSaved.class);
        INTERESTING.add(Text.class);
    }

    // Annotations that have been found relevant to mapping
    private final Map<Class<? extends Annotation>, Annotation> foundAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
    private final List<MappedField> typeParameters = new ArrayList<MappedField>();
    private Class persistedClass;
    private Field field; // the field :)
    private Class realType; // the real type
    private Constructor constructor; // the constructor for the type
    private Type subType; // the type (T) for the Collection<T>/T[]/Map<?,T>
    private Type mapKeyType; // the type (T) for the Map<T,?>
    private boolean isSingleValue = true; // indicates the field is a single value
    private boolean isMongoType;
    // indicated the type is a mongo compatible type (our version of value-type)
    private boolean isMap; // indicated if it implements Map interface
    private boolean isSet; // indicated if the collection is a set
    //for debugging
    private boolean isArray; // indicated if it is an Array
    private boolean isCollection; // indicated if the collection is a list)
    private Type genericType;

    MappedField(final Field f, final Class<?> clazz, final Mapper mapper) {
        f.setAccessible(true);
        field = f;
        persistedClass = clazz;
        realType = field.getType();
        genericType = field.getGenericType();
        discover(mapper);
    }


    MappedField(final Field field, final Type type, final Mapper mapper) {
        this.field = field;
        genericType = type;
        discoverType(mapper);
    }


    public static void addInterestingAnnotation(final Class<? extends Annotation> annotation) {
        INTERESTING.add(annotation);
    }


    public void addAnnotation(final Class<? extends Annotation> clazz) {
        if (field.isAnnotationPresent(clazz)) {
            foundAnnotations.put(clazz, field.getAnnotation(clazz));
        }
    }


    public void addAnnotation(final Class<? extends Annotation> clazz, final Annotation ann) {
        foundAnnotations.put(clazz, ann);
    }


    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        return (T) foundAnnotations.get(clazz);
    }


    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return foundAnnotations;
    }


    public Constructor getCTor() {
        return constructor;
    }


    public Class getConcreteType() {
        final Embedded e = getAnnotation(Embedded.class);
        if (e != null) {
            final Class concrete = e.concreteClass();
            if (concrete != Object.class) {
                return concrete;
            }
        }

        final Property p = getAnnotation(Property.class);
        if (p != null) {
            final Class concrete = p.concreteClass();
            if (concrete != Object.class) {
                return concrete;
            }
        }
        return getType();
    }


    public Object getDbObjectValue(final DBObject dbObj) {
        return dbObj.get(getFirstFieldName(dbObj));
    }


    public Class getDeclaringClass() {
        return field.getDeclaringClass();
    }


    public Field getField() {
        return field;
    }


    public Object getFieldValue(final Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public String getFirstFieldName(final DBObject dbObj) {
        String fieldName = getNameToStore();
        boolean foundField = false;
        for (final String n : getLoadNames()) {
            if (dbObj.containsField(n)) {
                if (!foundField) {
                    foundField = true;
                    fieldName = n;
                } else {
                    throw new MappingException(format("Found more than one field from @AlsoLoad %s", getLoadNames()));
                }
            }
        }
        return fieldName;
    }


    public String getFullName() {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }


    public String getJavaFieldName() {
        return field.getName();
    }


    public List<String> getLoadNames() {
        final List<String> names = new ArrayList<String>();
        names.add(getMappedFieldName());

        final AlsoLoad al = (AlsoLoad) foundAnnotations.get(AlsoLoad.class);
        if (al != null && al.value() != null && al.value().length > 0) {
            names.addAll(asList(al.value()));
        }

        return names;
    }


    public Class getMapKeyClass() {
        return toClass(mapKeyType);
    }


    public String getNameToStore() {
        return getMappedFieldName();
    }


    public Class getSubClass() {
        return toClass(subType);
    }


    public Type getSubType() {
        return subType;
    }


    public boolean isTransient() {
        return hasAnnotation(Transient.class) || Modifier.isTransient(field.getModifiers());
    }

    void setSubType(final Type subType) {
        this.subType = subType;
    }


    public Class getType() {
        return realType;
    }


    public List<MappedField> getTypeParameters() {
        return typeParameters;
    }


    public boolean hasAnnotation(final Class ann) {
        return foundAnnotations.containsKey(ann);
    }


    public boolean isArray() {
        return isArray;
    }


    public boolean isMap() {
        return isMap;
    }


    public boolean isMultipleValues() {
        return !isSingleValue();
    }


    public boolean isReference() {
        return hasAnnotation(Reference.class) || Key.class == getConcreteType() || DBRef.class == getConcreteType();
    }


    public boolean isSet() {
        return isSet;
    }


    public boolean isSingleValue() {
        if (!isSingleValue && !isMap && !isSet && !isArray && !isCollection) {
            throw new RuntimeException("Not single, but none of the types that are not-single.");
        }
        return isSingleValue;
    }


    public boolean isTypeMongoCompatible() {
        return isMongoType;
    }


    public Annotation putAnnotation(final Annotation ann) {
        return foundAnnotations.put(ann.getClass(), ann);
    }


    public void setFieldValue(final Object instance, final Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getMappedFieldName()).append(" (");
        sb.append(" type:").append(realType.getSimpleName()).append(",");

        if (isSingleValue()) {
            sb.append(" single:true,");
        } else {
            sb.append(" multiple:true,");
            sb.append(" subtype:").append(getSubClass()).append(",");
        }
        if (isMap()) {
            sb.append(" map:true,");
            if (getMapKeyClass() != null) {
                sb.append(" map-key:").append(getMapKeyClass().getSimpleName());
            } else {
                sb.append(" map-key: class unknown! ");
            }
        }

        if (isSet()) {
            sb.append(" set:true,");
        }
        if (isCollection) {
            sb.append(" collection:true,");
        }
        if (isArray) {
            sb.append(" array:true,");
        }

        //remove last comma
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }

        sb.append("); ").append(foundAnnotations.toString());
        return sb.toString();
    }


    protected void discover(final Mapper mapper) {
        for (final Class<? extends Annotation> clazz : INTERESTING) {
            addAnnotation(clazz);
        }

        //type must be discovered before the constructor.
        discoverType(mapper);
        constructor = discoverConstructor();
        discoverMultivalued();

        // check the main type
        isMongoType = ReflectionUtils.isPropertyType(realType);

        // if the main type isn't supported by the Mongo, see if the subtype is.
        // works for T[], List<T>, Map<?, T>, where T is Long/String/etc.
        if (!isMongoType && subType != null) {
            isMongoType = ReflectionUtils.isPropertyType(subType);
        }

        if (!isMongoType && !isSingleValue && (subType == null || subType == Object.class)) {
            if (LOG.isWarnEnabled() && !mapper.getConverters().hasDbObjectConverter(this)) {
                LOG.warning(format("The multi-valued field '%s' is a possible heterogeneous collection. It cannot be verified. "
                                   + "Please declare a valid type to get rid of this warning. %s", getFullName(), subType));
            }
            isMongoType = true;
        }
    }

    @SuppressWarnings("unchecked")
    protected void discoverType(final Mapper mapper) {
        if (genericType instanceof TypeVariable) {
            realType = extractTypeVariable((TypeVariable) genericType);
        } else if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            final Type[] types = pt.getActualTypeArguments();
            realType = toClass(pt);

            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    typeParameters.add(new EphemeralMappedField((ParameterizedType) type, this, mapper));
                } else {
                    if (type instanceof WildcardType) {
                        type = ((WildcardType) type).getUpperBounds()[0];
                    }
                    typeParameters.add(new EphemeralMappedField(type, this, mapper));
                }
            }
        } else if (genericType instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType) genericType;
            final Type[] types = wildcardType.getUpperBounds();
            realType = toClass(types[0]);
        } else if (genericType instanceof Class) {
            realType = (Class) genericType;
        } else if (genericType instanceof GenericArrayType) {
            final Type genericComponentType = ((GenericArrayType) genericType).getGenericComponentType();
            if (genericComponentType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericComponentType;
                realType = toClass(genericType);

                final Type[] types = pt.getActualTypeArguments();
                for (Type type : types) {
                    if (type instanceof ParameterizedType) {
                        typeParameters.add(new EphemeralMappedField((ParameterizedType) type, this, mapper));
                    } else {
                        if (type instanceof WildcardType) {
                            type = ((WildcardType) type).getUpperBounds()[0];
                        }
                        typeParameters.add(new EphemeralMappedField(type, this, mapper));
                    }
                }
            } else {
                if (genericComponentType instanceof TypeVariable) {
                    realType = toClass(genericType);
                } else {
                    realType = (Class) genericComponentType;
                }
            }
        }

        if (Object.class.equals(realType) || Object[].class.equals(realType)) {
            if (LOG.isWarnEnabled()) {
                LOG.warning(format("Parameterized types are treated as untyped Objects. See field '%s' on %s", field.getName(),
                                   field.getDeclaringClass()));
            }
        }

        if (realType == null) {
            throw new MappingException(format("A type could not be found for the field %s.%s", getType(), getField()));
        }
    }

    private Class extractTypeVariable(final TypeVariable<?> type) {
        final Class typeArgument = ReflectionUtils.getTypeArgument(persistedClass, type);
        return typeArgument != null ? typeArgument : Object.class;
    }


    protected String getMappedFieldName() {
        if (hasAnnotation(Id.class)) {
            return Mapper.ID_KEY;
        } else if (hasAnnotation(Property.class)) {
            final Property mv = (Property) foundAnnotations.get(Property.class);
            if (!mv.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return mv.value();
            }
        } else if (hasAnnotation(Reference.class)) {
            final Reference mr = (Reference) foundAnnotations.get(Reference.class);
            if (!mr.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return mr.value();
            }
        } else if (hasAnnotation(Embedded.class)) {
            final Embedded me = (Embedded) foundAnnotations.get(Embedded.class);
            if (!me.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return me.value();
            }
        } else if (hasAnnotation(Serialized.class)) {
            final Serialized me = (Serialized) foundAnnotations.get(Serialized.class);
            if (!me.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return me.value();
            }
        } else if (hasAnnotation(Version.class)) {
            final Version me = (Version) foundAnnotations.get(Version.class);
            if (!me.value().equals(Mapper.IGNORED_FIELDNAME)) {
                return me.value();
            }
        }

        return field.getName();
    }

    protected Class toClass(final Type t) {
        if (t == null) {
            return null;
        } else if (t instanceof Class) {
            return (Class) t;
        } else if (t instanceof GenericArrayType) {
            final Type type = ((GenericArrayType) t).getGenericComponentType();
            Class aClass;
            if (type instanceof ParameterizedType) {
                aClass = (Class) ((ParameterizedType) type).getRawType();
            } else if (type instanceof TypeVariable) {
                aClass = ReflectionUtils.getTypeArgument(persistedClass, (TypeVariable<?>) type);
                if (aClass == null) {
                    aClass = Object.class;
                }
            } else {
                aClass = (Class) type;
            }
            return Array.newInstance(aClass, 0).getClass();
        } else if (t instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) t).getRawType();
        } else if (t instanceof WildcardType) {
            return (Class) ((WildcardType) t).getUpperBounds()[0];
        }

        throw new RuntimeException("Generic TypeVariable not supported!");

    }

    private Constructor discoverConstructor() {
        Class<?> type = null;
        // get the first annotation with a concreteClass that isn't Object.class
        for (final Annotation an : foundAnnotations.values()) {
            try {
                final Method m = an.getClass().getMethod("concreteClass");
                m.setAccessible(true);
                final Object o = m.invoke(an);
                //noinspection EqualsBetweenInconvertibleTypes
                if (o != null && !(o.equals(Object.class))) {
                    type = (Class) o;
                    break;
                }
            } catch (NoSuchMethodException e) {
                // do nothing
            } catch (IllegalArgumentException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warning("There should not be an argument", e);
                }
            } catch (Exception e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warning("", e);
                }
            }
        }

        if (type != null) {
            try {
                constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                if (!hasAnnotation(ConstructorArgs.class)) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warning("No usable constructor for " + type.getName(), e);
                    }
                }
            }
        } else {
            // see if we can create instances of the type used for declaration
            type = getType();

            // short circuit to avoid wasting time throwing an exception trying to get a constructor we know doesnt exist
            if (type == List.class || type == Map.class) {
                return null;
            }

            if (type != null) {
                try {
                    constructor = type.getDeclaredConstructor();
                    constructor.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    // never mind.
                } catch (SecurityException e) {
                    // never mind.
                }
            }
        }
        return constructor;
    }

    private void discoverMultivalued() {
        if (realType.isArray()
            || Collection.class.isAssignableFrom(realType)
            || Map.class.isAssignableFrom(realType)
            || GenericArrayType.class.isAssignableFrom(genericType.getClass())) {

            isSingleValue = false;

            isMap = Map.class.isAssignableFrom(realType);
            isSet = Set.class.isAssignableFrom(realType);
            //for debugging
            isCollection = Collection.class.isAssignableFrom(realType);
            isArray = realType.isArray();

            //for debugging with issue
            if (!isMap && !isSet && !isCollection && !isArray) {
                throw new MappingException(format("%s.%s is not a map/set/collection/array : %s", field.getName(),
                                                  field.getDeclaringClass(), realType));
            }

            // get the subtype T, T[]/List<T>/Map<?,T>; subtype of Long[], List<Long> is Long
            subType = (realType.isArray()) ? realType.getComponentType() : ReflectionUtils.getParameterizedType(field, isMap ? 1 : 0);

            if (isMap) {
                mapKeyType = ReflectionUtils.getParameterizedType(field, 0);
            }
        }
    }

    void setIsMap(final boolean isMap) {
        this.isMap = isMap;
    }

    void setIsMongoType(final boolean isMongoType) {
        this.isMongoType = isMongoType;
    }

    void setIsSet(final boolean isSet) {
        this.isSet = isSet;
    }

    void setMapKeyType(final Class mapKeyType) {
        this.mapKeyType = mapKeyType;
    }
}
