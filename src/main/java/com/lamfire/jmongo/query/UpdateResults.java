package com.lamfire.jmongo.query;


import com.mongodb.WriteResult;

import static java.lang.String.format;


public class UpdateResults {
    private final WriteResult wr;


    public UpdateResults(final WriteResult wr) {
        this.wr = wr;
    }


    public int getInsertedCount() {
        return !getUpdatedExisting() ? getN() : 0;
    }


    public Object getNewId() {
        return wr.getUpsertedId();
    }


    public int getUpdatedCount() {
        return getUpdatedExisting() ? getN() : 0;
    }


    public boolean getUpdatedExisting() {
        return wr.isUpdateOfExisting();
    }


    public WriteResult getWriteResult() {
        return wr;
    }


    protected int getN() {
        return wr.getN();
    }

    @Override
    public String toString() {
        return format("UpdateResults{wr=%s}", wr);
    }
}
