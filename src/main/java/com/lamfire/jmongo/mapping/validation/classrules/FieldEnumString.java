package com.lamfire.jmongo.mapping.validation.classrules;


import com.lamfire.jmongo.mapping.MappedField;

import java.util.Arrays;
import java.util.List;



public class FieldEnumString {
    private final String display;


    public FieldEnumString(final MappedField... fields) {
        this(Arrays.asList(fields));
    }


    public FieldEnumString(final List<MappedField> fields) {
        final StringBuilder sb = new StringBuilder(128);
        for (final MappedField mappedField : fields) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(mappedField.getNameToStore());
        }
        display = sb.toString();
    }

    @Override
    public String toString() {
        return display;
    }
}
