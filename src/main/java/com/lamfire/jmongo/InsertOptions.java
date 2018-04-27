

package com.lamfire.jmongo;

import com.mongodb.WriteConcern;


public class InsertOptions {
    private com.mongodb.InsertOptions options = new com.mongodb.InsertOptions();


    public InsertOptions copy() {
        return new InsertOptions()
            .bypassDocumentValidation(getBypassDocumentValidation())
            .continueOnError(isContinueOnError())
            .writeConcern(getWriteConcern());
    }

    com.mongodb.InsertOptions getOptions() {
        return options;
    }


    public InsertOptions writeConcern(final WriteConcern writeConcern) {
        options.writeConcern(writeConcern);
        return this;
    }


    public InsertOptions continueOnError(final boolean continueOnError) {
        options.continueOnError(continueOnError);
        return this;
    }


    public WriteConcern getWriteConcern() {
        return options.getWriteConcern();
    }


    public boolean isContinueOnError() {
        return options.isContinueOnError();
    }


    public Boolean getBypassDocumentValidation() {
        return options.getBypassDocumentValidation();
    }


    public InsertOptions bypassDocumentValidation(final Boolean bypassDocumentValidation) {
        options.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }
}
