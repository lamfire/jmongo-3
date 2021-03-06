package com.lamfire.jmongo.query.validation;

import java.util.List;
import java.util.Map;


final class CollectionTypeValidator implements Validator {
    private CollectionTypeValidator() {
    }

    static boolean typeIsIterableOrArrayOrMap(final Class<?> type) {
        return typeIsAListOrArray(type) || typeIsIterable(type) || typeIsMap(type);
    }

    static boolean typeIsAListOrArray(final Class<?> type) {
        return (List.class.isAssignableFrom(type) || type.isArray());
    }

    static boolean typeIsIterable(final Class<?> type) {
        return Iterable.class.isAssignableFrom(type);
    }

    static boolean typeIsMap(final Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }
}
