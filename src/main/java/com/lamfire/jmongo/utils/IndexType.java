package com.lamfire.jmongo.utils;


public enum IndexType {
    ASC(1),
    DESC(-1),
    GEO2D("2d"),
    GEO2DSPHERE("2dsphere"),
    HASHED("hashed"),
    TEXT("text");

    private final Object type;

    IndexType(final Object o) {
        type = o;
    }


    public static IndexType fromValue(final Object value) {
        for (IndexType indexType : values()) {
            if (indexType.type.equals(value)) {
                return indexType;
            }
        }
        throw new IllegalArgumentException("No enum value found for " + value);
    }


    public Object toIndexValue() {
        return type;
    }
}
