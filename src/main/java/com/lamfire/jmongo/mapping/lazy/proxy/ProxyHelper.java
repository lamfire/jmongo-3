package com.lamfire.jmongo.mapping.lazy.proxy;



public final class ProxyHelper {

    private ProxyHelper() {
    }


    @SuppressWarnings("unchecked")
    public static <T> T unwrap(final T entity) {
        if (isProxy(entity)) {
            return (T) asProxy(entity).__unwrap();
        }
        return entity;
    }


    public static boolean isProxy(final Object entity) {
        return (entity != null && isProxied(entity.getClass()));
    }

    private static <T> ProxiedReference asProxy(final T entity) {
        return ((ProxiedReference) entity);
    }


    public static boolean isProxied(final Class<?> clazz) {
        return ProxiedReference.class.isAssignableFrom(clazz);
    }


    public static Class getReferentClass(final Object entity) {
        if (isProxy(entity)) {
            return asProxy(entity).__getReferenceObjClass();
        } else {
            return entity != null ? entity.getClass() : null;
        }
    }


    public static boolean isUnFetched(final Object entity) {
        return !isFetched(entity);
    }


    public static boolean isFetched(final Object entity) {
        return entity == null || !isProxy(entity) || asProxy(entity).__isFetched();
    }
}
