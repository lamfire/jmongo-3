package com.lamfire.jmongo;


import java.io.Serializable;
import java.util.Arrays;


public class Key<T> implements Serializable, Comparable<Key<T>> {
    private String collection;
    private Class<? extends T> type;


    private Object id;
    private byte[] idBytes;


    protected Key() {
    }


    public Key(final Class<? extends T> type, final String collection, final Object id) {
        this.type = type;
        this.collection = collection;
        this.id = id;
    }


    public Key(final Class<? extends T> type, final String collection, final byte[] idBytes) {
        this.type = type;
        this.collection = collection;
        this.idBytes = Arrays.copyOf(idBytes, idBytes.length);
    }


    @SuppressWarnings("unchecked")
    private static int compareNullable(final Comparable o1, final Comparable o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return o1.compareTo(o2);
        }
    }

    @Override
    public int compareTo(final Key<T> other) {
        checkState(this);
        checkState(other);

        int cmp;
        // First collection
        if (other.type != null && type != null) {
            cmp = type.getName().compareTo(other.type.getName());
            if (cmp != 0) {
                return cmp;
            }
        }
        cmp = compareNullable(collection, other.collection);
        if (cmp != 0) {
            return cmp;
        }

        try {
            cmp = compareNullable((Comparable<?>) id, (Comparable<?>) other.id);
            if (cmp != 0) {
                return cmp;
            }
        } catch (Exception e) {
            // Not a comparable, use equals and String.compareTo().
            cmp = id.equals(other.id) ? 0 : 1;
            if (cmp != 0) {
                return id.toString().compareTo(other.id.toString());
            }
        }

        return 0;
    }


    public String getCollection() {
        return collection;
    }


    public void setCollection(final String collection) {
        this.collection = collection.intern();
    }


    public Object getId() {
        return id;
    }


    public Class<? extends T> getType() {
        return type;
    }


    public void setType(final Class<? extends T> clazz) {
        type = clazz;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof Key<?> && compareTo((Key<T>) obj) == 0;

    }

    @Override
    public String toString() {
        final StringBuilder bld = new StringBuilder("Key{");

        if (collection != null) {
            bld.append("collection=");
            bld.append(collection);
        } else {
            bld.append("type=");
            bld.append(type.getName());
        }
        bld.append(", id=");
        bld.append(id);
        bld.append("}");

        return bld.toString();
    }

    private void checkState(final Key k) {
        if (k.type == null && k.collection == null) {
            throw new IllegalStateException("Collection must be specified (or a class).");
        }
        if (k.id == null && k.idBytes == null) {
            throw new IllegalStateException("id must be specified");
        }
    }
}
