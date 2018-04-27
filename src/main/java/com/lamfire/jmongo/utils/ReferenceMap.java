


package com.lamfire.jmongo.utils;

//CHECKSTYLE:OFF

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;



public class ReferenceMap extends AbstractMap {



    public static final int HARD = 0;

    public static final int SOFT = 1;

    public static final int WEAK = 2;

    private static final long serialVersionUID = -3370601314380922368L;


    // --- serialized instance variables:

    private int keyType;



    private int valueType;



    private float loadFactor;


    // -- Non-serialized instance variables


    private transient ReferenceQueue queue = new ReferenceQueue();



    private transient Entry[] table;



    private transient int size;



    private transient int threshold;



    private transient volatile int modCount;



    private transient Set keySet;



    private transient Set entrySet;



    private transient Collection values;



    public ReferenceMap() {
        this(HARD, SOFT);
    }



    public ReferenceMap(final int keyType, final int valueType) {
        this(keyType, valueType, 16, 0.75f);
    }



    public ReferenceMap(final int keyType, final int valueType, final int capacity, final float loadFactor) {

        verify("keyType", keyType);
        verify("valueType", valueType);

        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        if ((loadFactor <= 0.0f) || (loadFactor >= 1.0f)) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and less than 1.");
        }

        this.keyType = keyType;
        this.valueType = valueType;

        int v = 1;
        while (v < capacity) {
            v *= 2;
        }

        table = new Entry[v];
        this.loadFactor = loadFactor;
        threshold = (int) (v * loadFactor);
    }


    // used by constructor
    private static void verify(final String name, final int type) {
        if ((type < HARD) || (type > WEAK)) {
            throw new IllegalArgumentException(name + " must be HARD, SOFT, WEAK.");
        }
    }


    @Override
    public int size() {
        purge();
        return size;
    }


    @Override
    public boolean isEmpty() {
        purge();
        return size == 0;
    }


    @Override
    public boolean containsKey(final Object key) {
        purge();
        final Entry entry = getEntry(key);
        if (entry == null) {
            return false;
        }
        return entry.getValue() != null;
    }


    @Override
    public Object get(final Object key) {
        purge();
        final Entry entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }


    @Override
    public Object put(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException("null keys not allowed");
        }
        if (value == null) {
            throw new NullPointerException("null values not allowed");
        }

        purge();
        if (size + 1 > threshold) {
            resize();
        }

        final int hash = key.hashCode();
        final int index = indexFor(hash);
        Entry entry = table[index];
        while (entry != null) {
            if ((hash == entry.hash) && key.equals(entry.getKey())) {
                final Object result = entry.getValue();
                entry.setValue(value);
                return result;
            }
            entry = entry.next;
        }
        size++;
        modCount++;
        final Object keyRef = toReference(keyType, key, hash);
        final Object valueRef = toReference(valueType, value, hash);
        table[index] = new Entry(keyRef, hash, valueRef, table[index]);
        return null;
    }


    @Override
    public Object remove(final Object key) {
        if (key == null) {
            return null;
        }
        purge();
        final int hash = key.hashCode();
        final int index = indexFor(hash);
        Entry previous = null;
        Entry entry = table[index];
        while (entry != null) {
            if ((hash == entry.hash) && key.equals(entry.getKey())) {
                if (previous == null) {
                    table[index] = entry.next;
                } else {
                    previous.next = entry.next;
                }
                size--;
                modCount++;
                return entry.getValue();
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }


    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        //noinspection StatementWithEmptyBody
        while (queue.poll() != null) {
            // drain the queue
        }
    }


    @Override
    public Set keySet() {
        if (keySet != null) {
            return keySet;
        }
        keySet = new AbstractSet() {
            @Override
            public Iterator iterator() {
                return new KeyIterator();
            }            @Override
            public int size() {
                return size;
            }



            @Override
            public boolean contains(final Object o) {
                return containsKey(o);
            }


            @Override
            public boolean remove(final Object o) {
                final Object r = ReferenceMap.this.remove(o);
                return r != null;
            }

            @Override
            public void clear() {
                ReferenceMap.this.clear();
            }

        };
        return keySet;
    }


    @Override
    public Collection values() {
        if (values != null) {
            return values;
        }
        values = new AbstractCollection() {
            @Override
            public int size() {
                return size;
            }

            @Override
            public void clear() {
                ReferenceMap.this.clear();
            }

            @Override
            public Iterator iterator() {
                return new ValueIterator();
            }
        };
        return values;
    }


    @Override
    public Set entrySet() {
        if (entrySet != null) {
            return entrySet;
        }
        entrySet = new AbstractSet() {
            @Override
            public int size() {
                return ReferenceMap.this.size();
            }


            @Override
            public void clear() {
                ReferenceMap.this.clear();
            }


            @Override
            public boolean contains(final Object o) {
                if (o == null) {
                    return false;
                }
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                final Map.Entry e = (Map.Entry) o;
                final Entry e2 = getEntry(e.getKey());
                return (e2 != null) && e.equals(e2);
            }


            @Override
            public boolean remove(final Object o) {
                final boolean r = contains(o);
                if (r) {
                    final Map.Entry e = (Map.Entry) o;
                    ReferenceMap.this.remove(e.getKey());
                }
                return r;
            }


            @Override
            public Iterator iterator() {
                return new EntryIterator();
            }

            @Override
            public Object[] toArray() {
                return toArray(new Object[0]);
            }


            @Override
            @SuppressWarnings("unchecked")
            public Object[] toArray(final Object[] arr) {
                final List list = new ArrayList();
                final Iterator iterator = iterator();
                while (iterator.hasNext()) {
                    final Entry e = (Entry) iterator.next();
                    list.add(new DefaultMapEntry(e.getKey(), e.getValue()));
                }
                return list.toArray(arr);
            }
        };
        return entrySet;
    }


    private Entry getEntry(final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = key.hashCode();
        final int index = indexFor(hash);
        for (Entry entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && key.equals(entry.getKey())) {
                return entry;
            }
        }
        return null;
    }


    private int indexFor(final int toHash) {
        // mix the bits to avoid bucket collisions...
        int hash = ~(toHash << 15);
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += ~(hash << 11);
        hash ^= (hash >>> 16);
        return hash & (table.length - 1);
    }


    private void purge() {
        Reference ref = queue.poll();
        while (ref != null) {
            purge(ref);
            ref = queue.poll();
        }
    }

    private void purge(final Reference ref) {
        // The hashCode of the reference is the hashCode of the
        // mapping key, even if the reference refers to the
        // mapping value...
        final int hash = ref.hashCode();
        final int index = indexFor(hash);
        Entry previous = null;
        Entry entry = table[index];
        while (entry != null) {
            if (entry.purge(ref)) {
                if (previous == null) {
                    table[index] = entry.next;
                } else {
                    previous.next = entry.next;
                }
                size--;
                return;
            }
            previous = entry;
            entry = entry.next;
        }

    }


    private void readObject(final ObjectInputStream inp) throws IOException, ClassNotFoundException {
        inp.defaultReadObject();
        table = new Entry[inp.readInt()];
        threshold = (int) (table.length * loadFactor);
        queue = new ReferenceQueue();
        Object key = inp.readObject();
        while (key != null) {
            final Object value = inp.readObject();
            put(key, value);
            key = inp.readObject();
        }
    }


    private void resize() {
        final Entry[] old = table;
        table = new Entry[old.length * 2];

        for (int i = 0; i < old.length; i++) {
            Entry next = old[i];
            while (next != null) {
                final Entry entry = next;
                next = next.next;
                final int index = indexFor(entry.hash);
                entry.next = table[index];
                table[index] = entry;
            }
            old[i] = null;
        }
        threshold = (int) (table.length * loadFactor);
    }


    private Object toReference(final int type, final Object referent, final int hash) {
        switch (type) {
            case HARD:
                return referent;
            case SOFT:
                return new SoftRef(hash, referent, queue);
            case WEAK:
                return new WeakRef(hash, referent, queue);
            default:
                throw new Error();
        }
    }


    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(table.length);

        // Have to use null-terminated list because size might shrink
        // during iteration

        for (final Object o : entrySet()) {
            final Map.Entry entry = (Map.Entry) o;
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
        out.writeObject(null);
    }

    private static class SoftRef extends SoftReference {
        private final int hash;


        @SuppressWarnings("unchecked")
        public SoftRef(final int hash, final Object r, final ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }


        public int hashCode() {
            return hash;
        }
    }

    private static class WeakRef extends WeakReference {
        private final int hash;


        @SuppressWarnings("unchecked")
        public WeakRef(final int hash, final Object r, final ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }


        public int hashCode() {
            return hash;
        }
    }

    // If getKey() or getValue() returns null, it means
    // the mapping is stale and should be removed.
    private class Entry implements Map.Entry {

        final Object key;
        final int hash;
        Object value;
        Entry next;


        public Entry(final Object key, final int hash, final Object value, final Entry next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }

        public Object getKey() {
            return (keyType > HARD) ? ((Reference) key).get() : key;
        }        public int hashCode() {
            final Object v = getValue();
            return hash ^ ((v == null) ? 0 : v.hashCode());
        }

        public Object getValue() {
            return (valueType > HARD) ? ((Reference) value).get() : value;
        }

        public Object setValue(final Object object) {
            final Object old = getValue();
            if (valueType > HARD) {
                ((Reference) value).clear();
            }
            value = toReference(valueType, object, hash);
            return old;
        }

        boolean purge(final Reference ref) {
            boolean r = (keyType > HARD) && (key == ref);
            r = r || ((valueType > HARD) && (value == ref));
            if (r) {
                if (keyType > HARD) {
                    ((Reference) key).clear();
                }
                if (valueType > HARD) {
                    ((Reference) value).clear();
                }
            }
            return r;
        }





        public boolean equals(final Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            final Map.Entry entry = (Map.Entry) o;
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            if ((key == null) || (value == null)) {
                return false;
            }
            return key.equals(getKey()) && value.equals(getValue());
        }


        public String toString() {
            return getKey() + "=" + getValue();
        }

    }

    private class EntryIterator implements Iterator {
        // These fields keep track of where we are in the table.
        int index;
        Entry entry;
        Entry previous;

        // These Object fields provide hard references to the
        // current and next entry; this assures that if hasNext()
        // returns true, next() will actually return a valid element.
        Object nextKey;
        Object nextValue;
        Object currentKey;
        Object currentValue;

        int expectedModCount;


        public EntryIterator() {
            index = (size() != 0 ? table.length : 0);
            // have to do this here!  size() invocation above
            // may have altered the modCount.
            expectedModCount = modCount;
        }


        public boolean hasNext() {
            checkMod();
            while (nextNull()) {
                Entry e = entry;
                int i = index;
                while ((e == null) && (i > 0)) {
                    i--;
                    e = table[i];
                }
                entry = e;
                index = i;
                if (e == null) {
                    currentKey = null;
                    currentValue = null;
                    return false;
                }
                nextKey = e.getKey();
                nextValue = e.getValue();
                if (nextNull()) {
                    entry = entry.next;
                }
            }
            return true;
        }

        public Object next() {
            return nextEntry();
        }

        public void remove() {
            checkMod();
            if (previous == null) {
                throw new IllegalStateException();
            }
            ReferenceMap.this.remove(currentKey);
            previous = null;
            currentKey = null;
            currentValue = null;
            expectedModCount = modCount;
        }

        protected Entry nextEntry() {
            checkMod();
            if (nextNull() && !hasNext()) {
                throw new NoSuchElementException();
            }
            previous = entry;
            entry = entry.next;
            currentKey = nextKey;
            currentValue = nextValue;
            nextKey = null;
            nextValue = null;
            return previous;
        }

        private void checkMod() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private boolean nextNull() {
            return (nextKey == null) || (nextValue == null);
        }

    }


    // These two classes store the hashCode of the key of
    // of the mapping, so that after they're dequeued a quick
    // lookup of the bucket in the table can occur.

    private class ValueIterator extends EntryIterator {
        @Override
        public Object next() {
            return nextEntry().getValue();
        }
    }

    private class KeyIterator extends EntryIterator {
        @Override
        public Object next() {
            return nextEntry().getKey();
        }
    }

}
