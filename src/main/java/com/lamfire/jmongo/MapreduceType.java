package com.lamfire.jmongo;


import com.mongodb.MapReduceCommand.OutputType;

public enum MapreduceType {
    REPLACE,
    MERGE,
    REDUCE,
    INLINE;


    public static MapreduceType fromString(final String value) {
        for (int i = 0; i < values().length; i++) {
            final MapreduceType fo = values()[i];
            if (fo.name().equals(value)) {
                return fo;
            }
        }
        return null;
    }

    OutputType toOutputType() {
        switch (this) {
            case REDUCE:
                return OutputType.REDUCE;
            case MERGE:
                return OutputType.MERGE;
            case INLINE:
                return OutputType.INLINE;
            default:
                return OutputType.REPLACE;
        }

    }
}
