

package com.lamfire.jmongo.query;



import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionCountOptions;

import java.util.concurrent.TimeUnit;

import static com.mongodb.assertions.Assertions.notNull;


public class CountOptions {
    private DBCollectionCountOptions options = new DBCollectionCountOptions();


    public CountOptions collation(final Collation collation) {
        options.collation(collation);
        return this;
    }


    public Collation getCollation() {
        return options.getCollation();
    }


    public String getHint() {
        return options.getHintString();
    }


    public int getLimit() {
        return options.getLimit();
    }


    public long getMaxTime(final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        return options.getMaxTime(timeUnit);
    }


    public ReadConcern getReadConcern() {
        return options.getReadConcern();
    }


    public ReadPreference getReadPreference() {
        return options.getReadPreference();
    }


    public int getSkip() {
        return options.getSkip();
    }


    public CountOptions hint(final String hint) {
        options.hintString(hint);
        return this;
    }


    public CountOptions limit(final int limit) {
        options.limit(limit);
        return this;
    }


    public CountOptions maxTime(final long maxTime, final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        options.maxTime(maxTime, timeUnit);
        return this;
    }


    public CountOptions readConcern(final ReadConcern readConcern) {
        options.readConcern(readConcern);
        return this;
    }


    public CountOptions readPreference(final ReadPreference readPreference) {
        options.readPreference(readPreference);
        return this;
    }


    public CountOptions skip(final int skip) {
        options.skip(skip);
        return this;
    }

    DBCollectionCountOptions getOptions() {
        return options;
    }
}

