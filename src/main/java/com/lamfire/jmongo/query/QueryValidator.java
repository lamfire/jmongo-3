package com.lamfire.jmongo.query;

import com.lamfire.jmongo.annotations.Serialized;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.validation.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

final class QueryValidator {
    private static final Logger LOG = LoggerFactory.get(QueryValidator.class);

    private QueryValidator() {
    }


    static MappedField validateQuery(final Class clazz, final Mapper mapper, final StringBuilder origProp, final FilterOperator op,
                                     final Object val, final boolean validateNames, final boolean validateTypes) {
        MappedField mf = null;
        final String prop = origProp.toString();
        boolean hasTranslations = false;

        if (!origProp.substring(0, 1).equals("$")) {
            final String[] parts = prop.split("\\.");
            if (clazz == null) {
                return null;
            }

            MappedClass mc = mapper.getMappedClass(clazz);
            //CHECKSTYLE:OFF
            for (int i = 0; ; ) {
                //CHECKSTYLE:ON
                final String part = parts[i];
                boolean fieldIsArrayOperator = part.equals("$") || part.matches("[0-9]+");

                mf = mc.getMappedField(part);

                //translate from java field name to stored field name
                if (mf == null && !fieldIsArrayOperator) {
                    mf = mc.getMappedFieldByJavaField(part);
                    if (validateNames && mf == null) {
                        throw new ValidationException(format("The field '%s' could not be found in '%s' while validating - %s; if "
                                                             + "you wish to continue please disable validation.", part,
                                                             mc.getClazz().getName(), prop
                                                            ));
                    }
                    hasTranslations = true;
                    if (mf != null) {
                        parts[i] = mf.getNameToStore();
                    }
                }

                i++;
                if (mf != null && mf.isMap()) {
                    //skip the map key validation, and move to the next part
                    i++;
                }

                if (i >= parts.length) {
                    break;
                }

                if (!fieldIsArrayOperator) {
                    //catch people trying to search/update into @Reference/@Serialized fields
                    if (validateNames && !canQueryPast(mf)) {
                        throw new ValidationException(format("Cannot use dot-notation past '%s' in '%s'; found while"
                                                             + " validating - %s", part, mc.getClazz().getName(), prop));
                    }

                    if (mf == null && (mc.isInterface() || !validateNames)) {
                        break;
                    } else if (mf == null) {
                        throw new ValidationException(format("The field '%s' could not be found in '%s'", prop, mc.getClazz().getName()));
                    }
                    //get the next MappedClass for the next field validation
                    mc = mapper.getMappedClass((mf.isSingleValue()) ? mf.getType() : mf.getSubClass());
                }
            }

            //record new property string if there has been a translation to any part
            if (hasTranslations) {
                origProp.setLength(0); // clear existing content
                origProp.append(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    origProp.append('.');
                    origProp.append(parts[i]);
                }
            }

            if (validateTypes && mf != null) {
                List<ValidationFailure> typeValidationFailures = new ArrayList<ValidationFailure>();
                boolean compatibleForType = isCompatibleForOperator(mc, mf, mf.getType(), op, val, typeValidationFailures);
                List<ValidationFailure> subclassValidationFailures = new ArrayList<ValidationFailure>();
                boolean compatibleForSubclass = isCompatibleForOperator(mc, mf, mf.getSubClass(), op, val, subclassValidationFailures);

                if ((mf.isSingleValue() && !compatibleForType)
                    || mf.isMultipleValues() && !(compatibleForSubclass || compatibleForType)) {

                    if (LOG.isWarnEnabled()) {
                        LOG.warning(format("The type(s) for the query/update may be inconsistent; using an instance of type '%s' "
                                           + "for the field '%s.%s' which is declared as '%s'", val.getClass().getName(),
                                           mf.getDeclaringClass().getName(), mf.getJavaFieldName(), mf.getType().getName()
                                          ));
                        typeValidationFailures.addAll(subclassValidationFailures);
                        LOG.warning("Validation warnings: \n" + typeValidationFailures);
                    }
                }
            }
        }
        return mf;
    }

    private static boolean canQueryPast(final MappedField mf) {
        return !(mf.isReference() || mf.hasAnnotation(Serialized.class));
    }


    static boolean isCompatibleForOperator(final MappedClass mappedClass, final MappedField mappedField, final Class<?> type,
                                           final FilterOperator op,
                                           final Object value, final List<ValidationFailure> validationFailures) {
        // TODO: it's really OK to have null values?  I think this is to prevent null pointers further down,
        // but I want to move the null check into the operations that care whether they allow nulls or not.
        if (value == null || type == null) {
            return true;
        }

        boolean validationApplied = ExistsOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || SizeOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || InOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || NotInOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || ModOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || GeoWithinOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || AllOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
                                    || KeyValueTypeValidator.getInstance().apply(type, value, validationFailures)
                                    || IntegerTypeValidator.getInstance().apply(type, value, validationFailures)
                                    || LongTypeValidator.getInstance().apply(type, value, validationFailures)
                                    || DoubleTypeValidator.getInstance().apply(type, value, validationFailures)
                                    || PatternValueValidator.getInstance().apply(type, value, validationFailures)
                                    || EntityAnnotatedValueValidator.getInstance().apply(type, value, validationFailures)
                                    || ListValueValidator.getInstance().apply(type, value, validationFailures)
                                    || EntityTypeAndIdValueValidator.getInstance()
                                                                    .apply(mappedClass, mappedField, value, validationFailures)
                                    || DefaultTypeValidator.getInstance().apply(type, value, validationFailures);

        return validationApplied && validationFailures.size() == 0;
    }

}
