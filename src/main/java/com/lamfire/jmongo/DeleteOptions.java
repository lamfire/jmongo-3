

package com.lamfire.jmongo;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionRemoveOptions;


public final class DeleteOptions {
    private final DBCollectionRemoveOptions options = new DBCollectionRemoveOptions();


    public DeleteOptions copy() {
        DeleteOptions deleteOptions = new DeleteOptions()
            .writeConcern(getWriteConcern());

        if (getCollation() != null) {
            deleteOptions.collation(Collation.builder(getCollation()).build());
        }

        return deleteOptions;
    }


    public Collation getCollation() {
        return options.getCollation();
    }


    public DeleteOptions collation(final Collation collation) {
        options.collation(collation);
        return this;
    }


    public WriteConcern getWriteConcern() {
        return options.getWriteConcern();
    }


    public DeleteOptions writeConcern(final WriteConcern writeConcern) {
        options.writeConcern(writeConcern);
        return this;
    }

    DBCollectionRemoveOptions getOptions() {
        return options;
    }
}
