package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.EntityInterceptor;
import com.lamfire.jmongo.annotations.*;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.validation.MappingValidator;
import com.lamfire.jmongo.utils.ReflectionUtils;
import com.mongodb.DBObject;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;


public class MappedClass {
    private static final Logger LOG = LoggerFactory.getLogger(MappedClass.class);

    private static final List<Class<? extends Annotation>> INTERESTING_ANNOTATIONS = new ArrayList<Class<? extends Annotation>>();

    @SuppressWarnings("unchecked")
    private static final List<Class<? extends Annotation>> LIFECYCLE_ANNOTATIONS = asList(PrePersist.class,
            PreSave.class,
            PreLoad.class,
            PostPersist.class,
            PostLoad.class);

    static {
        INTERESTING_ANNOTATIONS.add(Embedded.class);
        INTERESTING_ANNOTATIONS.add(Entity.class);
        INTERESTING_ANNOTATIONS.add(EntityListeners.class);
        INTERESTING_ANNOTATIONS.add(Version.class);
        INTERESTING_ANNOTATIONS.add(Converters.class);
        INTERESTING_ANNOTATIONS.add(Indexes.class);
        INTERESTING_ANNOTATIONS.add(Validation.class);
        INTERESTING_ANNOTATIONS.add(Field.class);
        INTERESTING_ANNOTATIONS.add(IndexOptions.class);
    }


    private final Map<Class<? extends Annotation>, List<Annotation>> foundAnnotations =
            new HashMap<Class<? extends Annotation>, List<Annotation>>();

    private final Map<Class<? extends Annotation>, List<ClassMethodPair>> lifecycleMethods =
            new HashMap<Class<? extends Annotation>, List<ClassMethodPair>>();

    private final List<MappedField> persistenceFields = new ArrayList<MappedField>();

    private final Class<?> clazz;

    private java.lang.reflect.Field idField;

    private Entity entityAn;
    private Embedded embeddedAn;
    private MapperOptions mapperOptions;
    private MappedClass superClass;
    private List<MappedClass> interfaces = new ArrayList<MappedClass>();


    public MappedClass(final Class<?> clazz, final Mapper mapper) {
        this.clazz = clazz;
        mapperOptions = mapper.getOptions();

        if (LOG.isTraceEnabled()) {
            LOG.trace("Creating MappedClass for " + clazz);
        }

        basicValidate();
        discover(mapper);

        if (LOG.isDebugEnabled()) {
            LOG.debug("MappedClass done: " + toString());
        }
    }

    public static boolean isSupportedType(final Class<?> clazz) {
        if (ReflectionUtils.isPropertyType(clazz)) {
            return true;
        }
        if (clazz.isArray() || Map.class.isAssignableFrom(clazz) || Iterable.class.isAssignableFrom(clazz)) {
            Class<?> subType;
            if (clazz.isArray()) {
                subType = clazz.getComponentType();
            } else {
                subType = ReflectionUtils.getParameterizedClass(clazz);
            }

            //get component type, String.class from List<String>
            if (subType != null && subType != Object.class && !ReflectionUtils.isPropertyType(subType)) {
                return false;
            }

            //either no componentType or it is an allowed type
            return true;
        }
        return false;
    }

    public static void addInterestingAnnotation(final Class<? extends Annotation> annotation) {
        INTERESTING_ANNOTATIONS.add(annotation);
    }

    public MappedClass getSuperClass() {
        return superClass;
    }

    public boolean isInterface() {
        return clazz.isInterface();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public void addAnnotation(final Class<? extends Annotation> clazz, final Annotation ann) {
        if (ann == null || clazz == null) {
            return;
        }

        if (!foundAnnotations.containsKey(clazz)) {
            foundAnnotations.put(clazz, new ArrayList<Annotation>());
        }

        foundAnnotations.get(clazz).add(ann);
    }


    @SuppressWarnings({"WMI", "unchecked"})
    public DBObject callLifecycleMethods(final Class<? extends Annotation> event, final Object entity, final DBObject dbObj,
                                         final Mapper mapper) {
        final List<ClassMethodPair> methodPairs = getLifecycleMethods((Class<Annotation>) event);
        DBObject retDbObj = dbObj;
        try {
            Object tempObj;
            if (methodPairs != null) {
                final HashMap<Class<?>, Object> toCall = new HashMap<Class<?>, Object>((int) (methodPairs.size() * 1.3));
                for (final ClassMethodPair cm : methodPairs) {
                    toCall.put(cm.clazz, null);
                }
                for (final Class<?> c : toCall.keySet()) {
                    if (c != null) {
                        toCall.put(c, getOrCreateInstance(c, mapper));
                    }
                }

                for (final ClassMethodPair cm : methodPairs) {
                    final Method method = cm.method;
                    final Object inst = toCall.get(cm.clazz);
                    method.setAccessible(true);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(format("Calling lifecycle method(@%s %s) on %s", event.getSimpleName(), method, inst));
                    }

                    if (inst == null) {
                        if (method.getParameterTypes().length == 0) {
                            tempObj = method.invoke(entity);
                        } else {
                            tempObj = method.invoke(entity, retDbObj);
                        }
                    } else if (method.getParameterTypes().length == 0) {
                        tempObj = method.invoke(inst);
                    } else if (method.getParameterTypes().length == 1) {
                        tempObj = method.invoke(inst, entity);
                    } else {
                        tempObj = method.invoke(inst, entity, retDbObj);
                    }

                    if (tempObj != null) {
                        retDbObj = (DBObject) tempObj;
                    }
                }
            }

            callGlobalInterceptors(event, entity, dbObj, mapper);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return retDbObj;
    }


    public boolean containsJavaFieldName(final String name) {
        return getMappedField(name) != null;
    }


    public Annotation getAnnotation(final Class<? extends Annotation> clazz) {
        final List<Annotation> found = foundAnnotations.get(clazz);
        return found == null || found.isEmpty() ? null : found.get(found.size() - 1);
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> getAnnotations(final Class<? extends Annotation> clazz) {
        return (List<T>) foundAnnotations.get(clazz);
    }


    public Class<?> getClazz() {
        return clazz;
    }


    public String getCollectionName() {
        if (entityAn == null || entityAn.value().equals(Mapper.IGNORED_FIELDNAME)) {
            return mapperOptions.isUseLowerCaseCollectionNames() ? clazz.getSimpleName().toLowerCase() : clazz.getSimpleName();
        }
        return entityAn.value();
    }


    public Embedded getEmbeddedAnnotation() {
        return embeddedAn;
    }


    public Entity getEntityAnnotation() {
        return entityAn;
    }


    public List<MappedField> getFieldsAnnotatedWith(final Class<? extends Annotation> clazz) {
        final List<MappedField> results = new ArrayList<MappedField>();
        for (final MappedField mf : persistenceFields) {
            if (mf.getAnnotations().containsKey(clazz)) {
                results.add(mf);
            }
        }
        return results;
    }


    public Annotation getFirstAnnotation(final Class<? extends Annotation> clazz) {
        final List<Annotation> found = foundAnnotations.get(clazz);
        return found == null || found.isEmpty() ? null : found.get(0);
    }


    public java.lang.reflect.Field getIdField() {
        return idField;
    }


    public List<ClassMethodPair> getLifecycleMethods(final Class<Annotation> clazz) {
        return lifecycleMethods.get(clazz);
    }


    public MappedField getMappedField(final String storedName) {
        for (final MappedField mf : persistenceFields) {
            for (final String n : mf.getLoadNames()) {
                if (storedName.equals(n)) {
                    return mf;
                }
            }
        }

        return null;
    }


    public MappedField getMappedFieldByJavaField(final String name) {
        for (final MappedField mf : persistenceFields) {
            if (name.equals(mf.getJavaFieldName())) {
                return mf;
            }
        }

        return null;
    }


    public MappedField getMappedIdField() {
        List<MappedField> fields = getFieldsAnnotatedWith(Id.class);
        return fields.isEmpty() ? null : fields.get(0);
    }


    public MappedField getMappedVersionField() {
        List<MappedField> fields = getFieldsAnnotatedWith(Version.class);
        return fields.isEmpty() ? null : fields.get(0);
    }


    public List<MappedField> getPersistenceFields() {
        return persistenceFields;
    }


    public Map<Class<? extends Annotation>, List<Annotation>> getRelevantAnnotations() {
        return foundAnnotations;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MappedClass that = (MappedClass) o;

        return clazz.equals(that.clazz);

    }

    boolean isSubType(final MappedClass mc) {
        return mc.equals(superClass) || interfaces.contains(mc);
    }

    @Override
    public String toString() {
        return "MappedClass - kind:" + getCollectionName() + " for " + getClazz().getName() + " fields:" + persistenceFields;
    }


    // TODO: Remove this and make these fields dynamic or auto-set some other way
    public void update() {
        embeddedAn = (Embedded) getAnnotation(Embedded.class);
        entityAn = (Entity) getFirstAnnotation(Entity.class);
        // polymorphicAn = (Polymorphic) getAnnotation(Polymorphic.class);
        final List<MappedField> fields = getFieldsAnnotatedWith(Id.class);
        if (fields != null && !fields.isEmpty()) {
            idField = fields.get(0).getField();
        }
    }


    @SuppressWarnings("deprecation")
    public void validate(final Mapper mapper) {
        new MappingValidator(mapper.getOptions().getObjectFactory()).validate(mapper, this);
    }

    protected void basicValidate() {
        final boolean isStatic = Modifier.isStatic(clazz.getModifiers());
        if (!isStatic && clazz.isMemberClass()) {
            throw new MappingException("Cannot use non-static inner class: " + clazz + ". Please make static.");
        }
    }


    protected void discover(final Mapper mapper) {
        for (final Class<? extends Annotation> c : INTERESTING_ANNOTATIONS) {
            addAnnotation(c);
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            superClass = mapper.getMappedClass(superclass);
        }
        for (Class<?> aClass : clazz.getInterfaces()) {
            interfaces.add(mapper.getMappedClass(aClass));
        }

        final List<Class<?>> lifecycleClasses = new ArrayList<Class<?>>();
        lifecycleClasses.add(clazz);

        final EntityListeners entityLisAnn = (EntityListeners) getAnnotation(EntityListeners.class);
        if (entityLisAnn != null && entityLisAnn.value().length != 0) {
            Collections.addAll(lifecycleClasses, entityLisAnn.value());
        }

        for (final Class<?> cls : lifecycleClasses) {
            for (final Method m : ReflectionUtils.getDeclaredAndInheritedMethods(cls)) {
                for (final Class<? extends Annotation> c : LIFECYCLE_ANNOTATIONS) {
                    if (m.isAnnotationPresent(c)) {
                        addLifecycleEventMethod(c, m, cls.equals(clazz) ? null : cls);
                    }
                }
            }
        }

        update();

        for (final java.lang.reflect.Field field : ReflectionUtils.getDeclaredAndInheritedFields(clazz, true)) {
            field.setAccessible(true);
            final int fieldMods = field.getModifiers();
            if (!isIgnorable(field, fieldMods, mapper)) {
                if (field.isAnnotationPresent(Id.class)) {
                    persistenceFields.add(new MappedField(field, clazz, mapper));
                    update();
                } else if (field.isAnnotationPresent(Property.class)
                        || field.isAnnotationPresent(Reference.class)
                        || field.isAnnotationPresent(Embedded.class)
                        || field.isAnnotationPresent(Serialized.class)
                        || isSupportedType(field.getType())
                        || ReflectionUtils.implementsInterface(field.getType(), Serializable.class)) {
                    persistenceFields.add(new MappedField(field, clazz, mapper));
                } else {
                    if (mapper.getOptions().getDefaultMapper() != null) {
                        persistenceFields.add(new MappedField(field, clazz, mapper));
                    } else if (LOG.isWarnEnabled()) {
                        LOG.warning(format("Ignoring (will not persist) field: %s.%s [type:%s]", clazz.getName(), field.getName(),
                                field.getType().getName()));
                    }
                }
            }
        }
    }


    private void addAnnotation(final Class<? extends Annotation> clazz) {
        final List<? extends Annotation> annotations = ReflectionUtils.getAnnotations(getClazz(), clazz);
        for (final Annotation ann : annotations) {
            addAnnotation(clazz, ann);
        }
    }

    private void addLifecycleEventMethod(final Class<? extends Annotation> lceClazz, final Method m, final Class<?> clazz) {
        final ClassMethodPair cm = new ClassMethodPair(clazz, m);
        if (lifecycleMethods.containsKey(lceClazz)) {
            lifecycleMethods.get(lceClazz).add(cm);
        } else {
            final List<ClassMethodPair> methods = new ArrayList<ClassMethodPair>();
            methods.add(cm);
            lifecycleMethods.put(lceClazz, methods);
        }
    }

    private void callGlobalInterceptors(final Class<? extends Annotation> event, final Object entity, final DBObject dbObj,
                                        final Mapper mapper) {
        for (final EntityInterceptor ei : mapper.getInterceptors()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling interceptor method " + event.getSimpleName() + " on " + ei);
            }

            if (event.equals(PreLoad.class)) {
                ei.preLoad(entity, dbObj, mapper);
            } else if (event.equals(PostLoad.class)) {
                ei.postLoad(entity, dbObj, mapper);
            } else if (event.equals(PrePersist.class)) {
                ei.prePersist(entity, dbObj, mapper);
            } else if (event.equals(PreSave.class)) {
                ei.preSave(entity, dbObj, mapper);
            } else if (event.equals(PostPersist.class)) {
                ei.postPersist(entity, dbObj, mapper);
            }
        }
    }

    private Object getOrCreateInstance(final Class<?> clazz, final Mapper mapper) {
        if (mapper.getInstanceCache().containsKey(clazz)) {
            return mapper.getInstanceCache().get(clazz);
        }

        final Object o = mapper.getOptions().getObjectFactory().createInstance(clazz);
        final Object nullO = mapper.getInstanceCache().put(clazz, o);
        if (nullO != null) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Race-condition, created duplicate class: " + clazz);
            }
        }

        return o;

    }

    private boolean isIgnorable(final java.lang.reflect.Field field, final int fieldMods, final Mapper mapper) {
        return field.isAnnotationPresent(Transient.class)
                || Modifier.isTransient(fieldMods)
                || field.isSynthetic() && Modifier.isTransient(fieldMods)
                || mapper.getOptions().isIgnoreFinals() && Modifier.isFinal(fieldMods);
    }

    private static class ClassMethodPair {
        private final Class<?> clazz;
        private final Method method;

        ClassMethodPair(final Class<?> c, final Method m) {
            clazz = c;
            method = m;
        }
    }

}
