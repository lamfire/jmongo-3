

package com.lamfire.jmongo;

import com.lamfire.jmongo.annotations.Collation;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;

class CollationBuilder extends AnnotationBuilder<Collation> implements Collation {
    @Override
    public Class<Collation> annotationType() {
        return Collation.class;
    }

    @Override
    public boolean backwards() {
        return get("backwards");
    }

    @Override
    public boolean caseLevel() {
        return get("caseLevel");
    }

    @Override
    public String locale() {
        return get("locale");
    }

    @Override
    public boolean normalization() {
        return get("normalization");
    }

    @Override
    public boolean numericOrdering() {
        return get("numericOrdering");
    }

    @Override
    public CollationAlternate alternate() {
        return get("alternate");
    }

    @Override
    public CollationCaseFirst caseFirst() {
        return get("caseFirst");
    }

    @Override
    public CollationMaxVariable maxVariable() {
        return get("maxVariable");
    }

    @Override
    public CollationStrength strength() {
        return get("strength");
    }

    CollationBuilder backwards(final boolean backwards) {
        put("backwards", backwards);
        return this;
    }

    CollationBuilder caseLevel(final boolean caseLevel) {
        put("caseLevel", caseLevel);
        return this;
    }

    CollationBuilder locale(final String locale) {
        put("locale", locale);
        return this;
    }

    CollationBuilder normalization(final boolean normalization) {
        put("normalization", normalization);
        return this;
    }

    CollationBuilder numericOrdering(final boolean numericOrdering) {
        put("numericOrdering", numericOrdering);
        return this;
    }

    CollationBuilder alternate(final CollationAlternate alternate) {
        put("alternate", alternate);
        return this;
    }

    CollationBuilder caseFirst(final CollationCaseFirst caseFirst) {
        put("caseFirst", caseFirst);
        return this;
    }

    CollationBuilder maxVariable(final CollationMaxVariable maxVariable) {
        put("maxVariable", maxVariable);
        return this;
    }

    CollationBuilder strength(final CollationStrength strength) {
        put("strength", strength);
        return this;
    }
}
