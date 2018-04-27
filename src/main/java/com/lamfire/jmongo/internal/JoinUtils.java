

package com.lamfire.jmongo.internal;

import java.util.List;


public final class JoinUtils {
    private JoinUtils() {

    }


    public static String join(final List<String> strings, final char delimiter) {
        StringBuilder builder = new StringBuilder();
        for (String element : strings) {
            if (builder.length() != 0) {
                builder.append(delimiter);
            }
            builder.append(element);
        }
        return builder.toString();
    }
}
