package com.lamfire.jmongo.annotations;

import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;

public @interface Collation {

    boolean backwards() default false;


    boolean caseLevel() default false;


    String locale();


    boolean normalization() default false;


    boolean numericOrdering() default false;


    CollationAlternate alternate() default CollationAlternate.NON_IGNORABLE;


    CollationCaseFirst caseFirst() default CollationCaseFirst.OFF;


    CollationMaxVariable maxVariable() default CollationMaxVariable.PUNCT;


    CollationStrength strength() default CollationStrength.TERTIARY;
}
