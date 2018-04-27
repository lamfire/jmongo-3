


package com.lamfire.jmongo.utils;


import com.lamfire.jmongo.Key;
import com.lamfire.jmongo.annotations.Embedded;
import com.lamfire.jmongo.annotations.Entity;
import com.lamfire.jmongo.logging.JmongoLoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.MappingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;



public final class ReflectionUtils {
    private static final Logger LOG = JmongoLoggerFactory.get(ReflectionUtils.class);


    private ReflectionUtils() {
    }


    public static Field[] getDeclaredAndInheritedFields(final Class type, final boolean returnFinalFields) {
        final List<Field> allFields = new ArrayList<Field>();
        allFields.addAll(getValidFields(type.getDeclaredFields(), returnFinalFields));
        Class parent = type.getSuperclass();
        while ((parent != null) && (parent != Object.class)) {
            allFields.addAll(getValidFields(parent.getDeclaredFields(), returnFinalFields));
            parent = parent.getSuperclass();
        }
        return allFields.toArray(new Field[allFields.size()]);
    }


    public static List<Field> getValidFields(final Field[] fields, final boolean returnFinalFields) {
        final List<Field> validFields = new ArrayList<Field>();
        // we ignore static and final fields
        for (final Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers()) && (returnFinalFields || !Modifier.isFinal(field.getModifiers()))) {
                validFields.add(field);
            }
        }
        return validFields;
    }


    public static List<Method> getDeclaredAndInheritedMethods(final Class type) {
        return getDeclaredAndInheritedMethods(type, new ArrayList<Method>());
    }

    private static List<Method> getDeclaredAndInheritedMethods(final Class type, final List<Method> methods) {
        if ((type == null) || (type == Object.class)) {
            return methods;
        }

        final Class parent = type.getSuperclass();
        final List<Method> list = getDeclaredAndInheritedMethods(parent, methods == null ? new ArrayList<Method>() : methods);

        for (final Method m : type.getDeclaredMethods()) {
            if (!Modifier.isStatic(m.getModifiers())) {
                list.add(m);
            }
        }

        return list;
    }

    //    public static boolean implementsAnyInterface(final Class type, final Class... interfaceClasses)
    //    {
    //        for (Class iF : interfaceClasses)
    //        {
    //            if (implementsInterface(type, iF))
    //            {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }


    public static boolean isIntegerType(final Class type) {
        return Arrays.<Class>asList(Integer.class, int.class, Long.class, long.class, Short.class, short.class, Byte.class,
                                    byte.class).contains(type);
    }


    public static boolean isPropertyType(final Type type) {
        if (type instanceof GenericArrayType) {
            return isPropertyType(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof ParameterizedType) {
            return isPropertyType(((ParameterizedType) type).getRawType());
        }
        return type instanceof Class && isPropertyType((Class) type);
    }


    public static Class getParameterizedClass(final Field field) {
        return getParameterizedClass(field, 0);
    }


    public static Class getParameterizedClass(final Field field, final int index) {
        if (field.getGenericType() instanceof ParameterizedType) {
            final ParameterizedType type = (ParameterizedType) field.getGenericType();
            if ((type.getActualTypeArguments() != null) && (type.getActualTypeArguments().length <= index)) {
                return null;
            }
            final Type paramType = type.getActualTypeArguments()[index];
            if (paramType instanceof GenericArrayType) {
                final Class arrayType = (Class) ((GenericArrayType) paramType).getGenericComponentType();
                return Array.newInstance(arrayType, 0)
                            .getClass();
            } else {
                if (paramType instanceof ParameterizedType) {
                    final ParameterizedType paramPType = (ParameterizedType) paramType;
                    return (Class) paramPType.getRawType();
                } else {
                    if (paramType instanceof TypeVariable) {
                        // TODO: Figure out what to do... Walk back up the to
                        // the parent class and try to get the variable type
                        // from the T/V/X
                        throw new MappingException("Generic Typed Class not supported:  <" + ((TypeVariable) paramType).getName() + "> = "
                                                   + ((TypeVariable) paramType).getBounds()[0]);
                    } else if (paramType instanceof Class) {
                        return (Class) paramType;
                    } else {
                        throw new MappingException("Unknown type... pretty bad... call for help, wave your hands... yeah!");
                    }
                }
            }
        }
        return getParameterizedClass(field.getType());
    }


    public static Type getParameterizedType(final Field field, final int index) {
        if (field != null) {
            if (field.getGenericType() instanceof ParameterizedType) {
                final ParameterizedType type = (ParameterizedType) field.getGenericType();
                if ((type.getActualTypeArguments() != null) && (type.getActualTypeArguments().length <= index)) {
                    return null;
                }
                final Type paramType = type.getActualTypeArguments()[index];
                if (paramType instanceof GenericArrayType) {
                    return paramType; //((GenericArrayType) paramType).getGenericComponentType();
                } else {
                    if (paramType instanceof ParameterizedType) {
                        return paramType;
                    } else {
                        if (paramType instanceof TypeVariable) {
                            // TODO: Figure out what to do... Walk back up the to
                            // the parent class and try to get the variable type
                            // from the T/V/X
                            // throw new MappingException("Generic Typed Class not supported:  <" + ((TypeVariable)
                            // paramType).getName() + "> = " + ((TypeVariable) paramType).getBounds()[0]);
                            return paramType;
                        } else if (paramType instanceof WildcardType) {
                            return paramType;
                        } else if (paramType instanceof Class) {
                            return paramType;
                        } else {
                            throw new MappingException("Unknown type... pretty bad... call for help, wave your hands... yeah!");
                        }
                    }
                }
            }

            // Not defined on field, but may be on class or super class...
            return getParameterizedClass(field.getType());
        }

        return null;
    }


    public static Class getParameterizedClass(final Class c) {
        return getParameterizedClass(c, 0);
    }


    public static Class getParameterizedClass(final Class c, final int index) {
        final TypeVariable[] typeVars = c.getTypeParameters();
        if (typeVars.length > 0) {
            final TypeVariable typeVariable = typeVars[index];
            final Type[] bounds = typeVariable.getBounds();

            final Type type = bounds[0];
            if (type instanceof Class) {
                return (Class) type; // broke for EnumSet, cause bounds contain
                // type instead of class
            } else {
                return null;
            }
        } else {
            Type superclass = c.getGenericSuperclass();
            if (superclass == null && c.isInterface()) {
                Type[] interfaces = c.getGenericInterfaces();
                if (interfaces.length > 0) {
                    superclass = interfaces[index];
                }
            }
            if (superclass instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
                return actualTypeArguments.length > index ? (Class<?>) actualTypeArguments[index] : null;
            } else if (!Object.class.equals(superclass)) {
                return getParameterizedClass((Class) superclass);
            } else {
                return null;
            }
        }
    }


    public static boolean isFieldParameterizedWithClass(final Field field, final Class c) {
        if (field.getGenericType() instanceof ParameterizedType) {
            final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            for (final Type type : genericType.getActualTypeArguments()) {
                if (type == c) {
                    return true;
                }
                if (c.isInterface() && implementsInterface((Class) type, c)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean implementsInterface(final Class<?> type, final Class<?> interfaceClass) {
        return interfaceClass.isAssignableFrom(type);
    }


    public static boolean isFieldParameterizedWithPropertyType(final Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            for (final Type type : genericType.getActualTypeArguments()) {
                if (isPropertyType((Class) type)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isPropertyType(final Class type) {
        return type != null && (isPrimitiveLike(type) || type == DBRef.class || type == Pattern.class
                                || type == CodeWScope.class || type == ObjectId.class || type == Key.class
                                || type == DBObject.class || type == BasicDBObject.class);

    }


    public static boolean isPrimitiveLike(final Class type) {
        return type != null && (type == String.class || type == char.class
                                || type == Character.class || type == short.class || type == Short.class
                                || type == Integer.class || type == int.class || type == Long.class || type == long.class
                                || type == Double.class || type == double.class || type == float.class || type == Float.class
                                || type == Boolean.class || type == boolean.class || type == Byte.class || type == byte.class
                                || type == Date.class || type == Locale.class || type == Class.class || type == UUID.class
                                || type == URI.class || type.isEnum());

    }


    public static Embedded getClassEmbeddedAnnotation(final Class c) {
        return getAnnotation(c, Embedded.class);
    }


    public static <T> T getAnnotation(final Class c, final Class<T> annotation) {
        final List<T> found = getAnnotations(c, annotation);
        if (found != null && !found.isEmpty()) {
            return found.get(0);
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> List<T> getAnnotations(final Class c, final Class<T> annotation) {
        final List<T> found = new ArrayList<T>();
        // TODO isn't that actually breaking the contract of @Inherited?
        if (c.isAnnotationPresent(annotation)) {
            found.add((T) c.getAnnotation(annotation));
        }

        Class parent = c.getSuperclass();
        while ((parent != null) && (parent != Object.class)) {
            if (parent.isAnnotationPresent(annotation)) {
                found.add((T) parent.getAnnotation(annotation));
            }

            // ...and interfaces that the superclass implements
            for (final Class interfaceClass : parent.getInterfaces()) {
                if (interfaceClass.isAnnotationPresent(annotation)) {
                    found.add((T) interfaceClass.getAnnotation(annotation));
                }
            }

            parent = parent.getSuperclass();
        }

        // ...and all implemented interfaces
        for (final Class interfaceClass : c.getInterfaces()) {
            if (interfaceClass.isAnnotationPresent(annotation)) {
                found.add((T) interfaceClass.getAnnotation(annotation));
            }
        }
        // no annotation found, use the defaults
        return found;
    }


    public static Entity getClassEntityAnnotation(final Class c) {
        return getAnnotation(c, Entity.class);
    }


    public static Set<Class<?>> getClasses(final String packageName) throws IOException, ClassNotFoundException {
        return getClasses(packageName, false);
    }

    public static Set<Class<?>> getClasses(final String packageName, final boolean mapSubPackages) throws IOException,
            ClassNotFoundException {
        final ClassLoader loader = Thread.currentThread()
                                         .getContextClassLoader();
        return getClasses(loader, packageName, mapSubPackages);
    }


    public static Set<Class<?>> getClasses(final ClassLoader loader, final String packageName) throws IOException, ClassNotFoundException {
        return getClasses(loader, packageName, false);
    }


    public static Set<Class<?>> getClasses(final ClassLoader loader, final String packageName, final boolean mapSubPackages) throws
            IOException, ClassNotFoundException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final String path = packageName.replace('.', '/');
        final Enumeration<URL> resources = loader.getResources(path);
        if (resources != null) {
            while (resources.hasMoreElements()) {
                String filePath = resources.nextElement()
                                           .getFile();
                // WINDOWS HACK
                if (filePath.indexOf("%20") > 0) {
                    filePath = filePath.replaceAll("%20", " ");
                }
                // # in the jar name
                if (filePath.indexOf("%23") > 0) {
                    filePath = filePath.replaceAll("%23", "#");
                }

                if (filePath != null) {
                    if ((filePath.indexOf("!") > 0) && (filePath.indexOf(".jar") > 0)) {
                        String jarPath = filePath.substring(0, filePath.indexOf("!"))
                                                 .substring(filePath.indexOf(":") + 1);
                        // WINDOWS HACK
                        if (jarPath.contains(":")) {
                            jarPath = jarPath.substring(1);
                        }
                        classes.addAll(getFromJARFile(loader, jarPath, path, mapSubPackages));
                    } else {
                        classes.addAll(getFromDirectory(loader, new File(filePath), packageName, mapSubPackages));
                    }
                }
            }
        }
        return classes;
    }


    public static Set<Class<?>> getFromJARFile(final ClassLoader loader, final String jar, final String packageName)
        throws IOException, ClassNotFoundException {
        return getFromJARFile(loader, jar, packageName, false);
    }


    public static Set<Class<?>> getFromJARFile(final ClassLoader loader, final String jar, final String packageName, final boolean
        mapSubPackages) throws IOException, ClassNotFoundException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
        try {
            JarEntry jarEntry;
            do {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry != null) {
                    String className = jarEntry.getName();
                    if (className.endsWith(".class")) {
                        String classPackageName = getPackageName(className);
                        if (classPackageName.equals(packageName) || (mapSubPackages && isSubPackage(classPackageName, packageName))) {
                            className = stripFilenameExtension(className);
                            classes.add(Class.forName(className.replace('/', '.'), true, loader));
                        }
                    }
                }
            } while (jarEntry != null);
        } finally {
            jarFile.close();
        }
        return classes;
    }


    public static Set<Class<?>> getFromDirectory(final ClassLoader loader, final File directory, final String packageName)
        throws ClassNotFoundException {
        return getFromDirectory(loader, directory, packageName, false);
    }


    public static Set<Class<?>> getFromDirectory(final ClassLoader loader, final File directory, final String packageName,
                                                            final boolean mapSubPackages) throws ClassNotFoundException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        if (directory.exists()) {
            for (final String file : getFileNames(directory, packageName, mapSubPackages)) {
                if (file.endsWith(".class")) {
                    final String name = stripFilenameExtension(file);
                    final Class<?> clazz = Class.forName(name, true, loader);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private static Set<String> getFileNames(final File directory, final String packageName, final boolean mapSubPackages) {
        Set<String> fileNames = new HashSet<String>();
        for (File file: directory.listFiles()) {
            if (file.isFile()) {
                fileNames.add(packageName + '.' + file.getName());
            } else if (mapSubPackages){
                fileNames.addAll(getFileNames(file, packageName + '.' + file.getName(), true));
            }
        }
        return fileNames;
    }

    private static String getPackageName(final String filename) {
        return filename.contains("/") ? filename.substring(0, filename.lastIndexOf('/')) : filename;
    }

    private static String stripFilenameExtension(final String filename) {
        if (filename.indexOf('.') != -1) {
            return filename.substring(0, filename.lastIndexOf('.'));
        } else {
            return filename;
        }
    }

    private static boolean isSubPackage(final String fullPackageName, final String parentPackageName) {
        return fullPackageName.startsWith(parentPackageName);
    }


    public static <T> List<T> iterToList(final Iterable<T> it) {
        if (it instanceof List) {
            return (List<T>) it;
        }
        if (it == null) {
            return null;
        }

        final List<T> ar = new ArrayList<T>();
        for (final T o : it) {
            ar.add(o);
        }

        return ar;
    }


    public static Object convertToArray(final Class type, final List<?> values) {
        final Object exampleArray = Array.newInstance(type, values.size());
        try {
            return values.toArray((Object[]) exampleArray);
        } catch (ClassCastException e) {
            for (int i = 0; i < values.size(); i++) {
                Array.set(exampleArray, i, values.get(i));
            }
            return exampleArray;
        }
    }



    public static Class<?> getClass(final Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            final Type componentType = ((GenericArrayType) type).getGenericComponentType();
            final Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                LOG.debug("************ ReflectionUtils.getClass 1st else");
                LOG.debug("************ type = " + type);
                return null;
            }
        } else {
            LOG.debug("************ ReflectionUtils.getClass final else");
            LOG.debug("************ type = " + type);
            return null;
        }
    }


    public static <T> List<Class<?>> getTypeArguments(final Class<T> baseClass, final Class<? extends T> childClass) {
        final Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just
                // keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Class<?> rawType = (Class) parameterizedType.getRawType();

                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass,
        // determine (if possible)
        // the raw class for that type argument.
        final Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        final List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }


    public static <T> Class<?> getTypeArgument(final Class<? extends T> clazz, final TypeVariable<? extends GenericDeclaration> tv) {
        final Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = clazz;
        // start walking up the inheritance hierarchy until we hit the end
        while (type != null && !Object.class.equals(getClass(type))) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just
                // keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Class<?> rawType = (Class) parameterizedType.getRawType();

                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    if (typeParameters[i].equals(tv)) {
                        final Class cls = getClass(actualTypeArguments[i]);
                        if (cls != null) {
                            return cls;
                        }
                        //We don't know that the type we want is the one in the map, if this argument has been
                        //passed through multiple levels of the hierarchy.  Walk back until we run out.
                        Type typeToTest = resolvedTypes.get(actualTypeArguments[i]);
                        while (typeToTest != null) {
                            final Class classToTest = getClass(typeToTest);
                            if (classToTest != null) {
                                return classToTest;
                            }
                            typeToTest = resolvedTypes.get(typeToTest);
                        }
                    }
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(Object.class)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        return null;
    }
}
