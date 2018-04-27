package com.lamfire.jmongo.mapping.validation;

import com.lamfire.jmongo.mapping.MappingException;

import java.util.Collection;



public class ConstraintViolationException extends MappingException {

    private static final long serialVersionUID = 1L;


    public ConstraintViolationException(final Collection<ConstraintViolation> ve) {
        super(createString(ve.toArray(new ConstraintViolation[ve.size()])));
    }


    public ConstraintViolationException(final ConstraintViolation... ve) {
        super(createString(ve));
    }

    private static String createString(final ConstraintViolation[] ve) {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("Number of violations: " + ve.length + " \n");
        for (final ConstraintViolation validationError : ve) {
            sb.append(validationError.render());
        }
        return sb.toString();
    }
}
