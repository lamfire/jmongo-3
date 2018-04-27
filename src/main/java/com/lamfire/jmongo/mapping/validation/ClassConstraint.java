package com.lamfire.jmongo.mapping.validation;


import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.Mapper;

import java.util.Set;



public interface ClassConstraint {

    void check(final Mapper mapper, MappedClass mc, Set<ConstraintViolation> ve);
}
