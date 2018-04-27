

package com.lamfire.jmongo;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionUpdateOptions;


public class UpdateOptions {
    private DBCollectionUpdateOptions options = new DBCollectionUpdateOptions();

    DBCollectionUpdateOptions getOptions() {
        return options;
    }


    public UpdateOptions copy() {
        return new UpdateOptions()
            .bypassDocumentValidation(getBypassDocumentValidation())
            .collation(getCollation())
            .multi(isMulti())
            .upsert(isUpsert())
            .writeConcern(getWriteConcern());
    }


    public boolean isUpsert() {
        return options.isUpsert();
    }


    public UpdateOptions upsert(final boolean isUpsert) {
        options.upsert(isUpsert);
        return this;
    }


    public Boolean getBypassDocumentValidation() {
        return options.getBypassDocumentValidation();
    }


    public UpdateOptions bypassDocumentValidation(final Boolean bypassDocumentValidation) {
        options.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }


    public UpdateOptions multi(final boolean multi) {
        options.multi(multi);
        return this;
    }


    public boolean isMulti() {
        return options.isMulti();
    }


    public Collation getCollation() {
        return options.getCollation();
    }


    public UpdateOptions collation(final Collation collation) {
        options.collation(collation);
        return this;
    }


    public WriteConcern getWriteConcern() {
        return options.getWriteConcern();
    }


    public UpdateOptions writeConcern(final WriteConcern writeConcern) {
        options.writeConcern(writeConcern);
        return this;
    }
}
