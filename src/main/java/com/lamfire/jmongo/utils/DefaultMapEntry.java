//CHECKSTYLE:OFF


package com.lamfire.jmongo.utils;


import java.util.Map;




public class DefaultMapEntry implements Map.Entry {

    private Object key;
    private Object value;


    public DefaultMapEntry() {
    }


    public DefaultMapEntry(final Object key, final Object value) {
        this.key = key;
        this.value = value;
    }


    public Object getKey() {
        return key;
    }


    public Object getValue() {
        return value;
    }


    // Map.Entry interface
    //-------------------------------------------------------------------------


    public Object setValue(final Object value) {
        final Object answer = this.value;
        this.value = value;
        return answer;
    }


    public void setKey(final Object key) {
        this.key = key;
    }

    // Properties
    //-------------------------------------------------------------------------


    public int hashCode() {
        return ((getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode()));
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
        final Map.Entry e2 = (Map.Entry) o;
        return ((getKey() == null ? e2.getKey() == null : getKey().equals(e2.getKey())) && (getValue() == null
                                                                                            ? e2.getValue() == null
                                                                                            : getValue().equals(e2.getValue())));
    }

}
