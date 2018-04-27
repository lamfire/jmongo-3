package com.lamfire.jmongo.utils;


import org.bson.BSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



public final class IterHelper<K, V> {

    @SuppressWarnings("unchecked")
    public void loopMap(final Object x, final MapIterCallback<K, V> callback) {
        if (x == null) {
            return;
        }

        if (x instanceof Collection) {
            throw new IllegalArgumentException("call loop instead");
        }

        if (x instanceof HashMap<?, ?>) {
            if (((HashMap) x).isEmpty()) {
                return;
            }

            final HashMap<?, ?> hm = (HashMap<?, ?>) x;
            for (final Entry<?, ?> e : hm.entrySet()) {
                callback.eval((K) e.getKey(), (V) e.getValue());
            }
            return;
        }
        if (x instanceof Map) {
            final Map<K, V> m = (Map<K, V>) x;
            for (final Entry<K, V> entry : m.entrySet()) {
                callback.eval(entry.getKey(), entry.getValue());
            }
            return;
        }
        if (x instanceof BSONObject) {
            final BSONObject m = (BSONObject) x;
            for (final String k : m.keySet()) {
                callback.eval((K) k, (V) m.get(k));
            }
        }

    }


    @SuppressWarnings({"unchecked"})
    public void loopOrSingle(final Object x, final IterCallback<V> callback) {
        if (x == null) {
            return;
        }

        //A collection
        if (x instanceof Collection<?>) {
            final Collection<?> l = (Collection<?>) x;
            for (final Object o : l) {
                callback.eval((V) o);
            }
            return;
        }

        //An array of Object[]
        if (x.getClass().isArray()) {
            for (final Object o : (Object[]) x) {
                callback.eval((V) o);
            }
            return;
        }

        callback.eval((V) x);
    }


    public abstract static class MapIterCallback<K, V> {

        public abstract void eval(K k, V v);
    }


    public abstract static class IterCallback<V> {

        public abstract void eval(V v);
    }
}
