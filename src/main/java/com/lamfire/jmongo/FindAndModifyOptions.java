

package com.lamfire.jmongo;

import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionFindAndModifyOptions;

import java.util.concurrent.TimeUnit;

import static com.mongodb.assertions.Assertions.notNull;


public final class FindAndModifyOptions {
    private DBCollectionFindAndModifyOptions options = new DBCollectionFindAndModifyOptions()
        .returnNew(true);

    FindAndModifyOptions copy() {
        FindAndModifyOptions copy = new FindAndModifyOptions();
        copy.bypassDocumentValidation(getBypassDocumentValidation());
        copy.collation(getCollation());
        copy.maxTime(getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        copy.projection(getProjection());
        copy.remove(isRemove());
        copy.returnNew(isReturnNew());
        copy.sort(getSort());
        copy.update(getUpdate());
        copy.upsert(isUpsert());
        copy.writeConcern(getWriteConcern());
        return copy;
    }

    DBCollectionFindAndModifyOptions getOptions() {
        return copy().options;
    }

    DBObject getProjection() {
        return options.getProjection();
    }

    FindAndModifyOptions projection(final DBObject projection) {
        options.projection(projection);
        return this;
    }


    DBObject getSort() {
        return options.getSort();
    }


    FindAndModifyOptions sort(final DBObject sort) {
        options.sort(sort);
        return this;
    }


    public boolean isRemove() {
        return options.isRemove();
    }


    public FindAndModifyOptions remove(final boolean remove) {
        options.remove(remove);
        return this;
    }


    DBObject getUpdate() {
        return options.getUpdate();
    }


    FindAndModifyOptions update(final DBObject update) {
        options.update(update);
        return this;
    }


    public boolean isUpsert() {
        return options.isUpsert();
    }


    public FindAndModifyOptions upsert(final boolean upsert) {
        options.upsert(upsert);
        return this;
    }


    public boolean isReturnNew() {
        return options.returnNew();
    }


    public FindAndModifyOptions returnNew(final boolean returnNew) {
        options.returnNew(returnNew);
        return this;
    }


    public Boolean getBypassDocumentValidation() {
        return options.getBypassDocumentValidation();
    }


    public FindAndModifyOptions bypassDocumentValidation(final Boolean bypassDocumentValidation) {
        options.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }


    public long getMaxTime(final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        return options.getMaxTime(timeUnit);
    }


    public FindAndModifyOptions maxTime(final long maxTime, final TimeUnit timeUnit) {
        options.maxTime(maxTime, timeUnit);
        return this;
    }


    public WriteConcern getWriteConcern() {
        return options.getWriteConcern();
    }


    public FindAndModifyOptions writeConcern(final WriteConcern writeConcern) {
        options.writeConcern(writeConcern);
        return this;
    }


    public Collation getCollation() {
        return options.getCollation();
    }


    public FindAndModifyOptions collation(final Collation collation) {
        options.collation(collation);
        return this;
    }
}
