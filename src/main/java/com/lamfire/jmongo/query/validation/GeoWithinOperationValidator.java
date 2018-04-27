package com.lamfire.jmongo.query.validation;

import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.query.FilterOperator;
import com.mongodb.DBObject;

import java.util.List;

import static com.lamfire.jmongo.query.FilterOperator.GEO_WITHIN;
import static com.lamfire.jmongo.query.validation.MappedFieldTypeValidator.isArrayOfNumbers;
import static com.lamfire.jmongo.query.validation.MappedFieldTypeValidator.isIterableOfNumbers;
import static java.lang.String.format;


public final class GeoWithinOperationValidator extends OperationValidator {
    private static final GeoWithinOperationValidator INSTANCE = new GeoWithinOperationValidator();

    private GeoWithinOperationValidator() {
    }


    public static GeoWithinOperationValidator getInstance() {
        return INSTANCE;
    }

    // this could be a lot more rigorous
    private static boolean isValueAValidGeoQuery(final Object value) {
        if (value instanceof DBObject) {
            String key = ((DBObject) value).keySet().iterator().next();
            return key.equals("$box") || key.equals("$center") || key.equals("$centerSphere") || key.equals("$polygon");
        }
        return false;
    }

    @Override
    protected FilterOperator getOperator() {
        return GEO_WITHIN;
    }

    @Override
    protected void validate(final MappedField mappedField, final Object value,
                            final List<ValidationFailure> validationFailures) {
        if (!isArrayOfNumbers(mappedField) && !isIterableOfNumbers(mappedField)) {
            validationFailures.add(new ValidationFailure(format("For a $geoWithin operation, if field '%s' is an array or iterable it "
                                                                + "should have numeric values. Instead it had: %s",
                                                                mappedField, mappedField.getSubClass()
                                                               )));

        }
        if (!isValueAValidGeoQuery(value)) {
            validationFailures.add(new ValidationFailure(format("For a $geoWithin operation, the value should be a valid geo query. "
                                                                + "Instead it was: %s", value)));
        }
    }
}
