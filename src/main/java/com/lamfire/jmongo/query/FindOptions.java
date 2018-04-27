

package com.lamfire.jmongo.query;

import com.mongodb.CursorType;
import com.mongodb.DBObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionFindOptions;

import java.util.concurrent.TimeUnit;


public class FindOptions {
    private DBCollectionFindOptions options = new DBCollectionFindOptions();


    public FindOptions() {
    }

    private FindOptions(final DBCollectionFindOptions copy) {
        options = copy.copy();
    }


    public FindOptions copy() {
        return new FindOptions(options.copy());
    }


    public int getLimit() {
        return options.getLimit();
    }


    public FindOptions limit(final int limit) {
        options.limit(limit);
        return this;
    }


    public int getSkip() {
        return options.getSkip();
    }


    public FindOptions skip(final int skip) {
        options.skip(skip);
        return this;
    }


    public long getMaxTime(final TimeUnit timeUnit) {
        return options.getMaxTime(timeUnit);
    }


    public FindOptions maxTime(final long maxTime, final TimeUnit timeUnit) {
        options.maxTime(maxTime, timeUnit);
        return this;
    }


    public long getMaxAwaitTime(final TimeUnit timeUnit) {
        return options.getMaxAwaitTime(timeUnit);
    }


    public FindOptions maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        options.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }


    public int getBatchSize() {
        return options.getBatchSize();
    }


    public FindOptions batchSize(final int batchSize) {
        options.batchSize(batchSize);
        return this;
    }


    DBObject getModifiers() {
        return options.getModifiers();
    }


    public FindOptions modifier(final String key, final Object value) {
        options.getModifiers().put(key, value);
        return this;
    }


    DBObject getProjection() {
        return options.getProjection();
    }


    FindOptions projection(final DBObject projection) {
        options.projection(projection);
        return this;
    }


    DBObject getSortDBObject() {
        return options.getSort();
    }


    FindOptions sort(final DBObject sort) {
        options.sort(sort);
        return this;
    }


    public boolean isNoCursorTimeout() {
        return options.isNoCursorTimeout();
    }


    public FindOptions noCursorTimeout(final boolean noCursorTimeout) {
        options.noCursorTimeout(noCursorTimeout);
        return this;
    }


    public boolean isOplogReplay() {
        return options.isOplogReplay();
    }


    public FindOptions oplogReplay(final boolean oplogReplay) {
        options.oplogReplay(oplogReplay);
        return this;
    }


    public boolean isPartial() {
        return options.isPartial();
    }


    public FindOptions partial(final boolean partial) {
        options.partial(partial);
        return this;
    }


    public CursorType getCursorType() {
        return options.getCursorType();
    }


    public FindOptions cursorType(final CursorType cursorType) {
        options.cursorType(cursorType);
        return this;
    }


    public ReadPreference getReadPreference() {
        return options.getReadPreference();
    }


    public FindOptions readPreference(final ReadPreference readPreference) {
        options.readPreference(readPreference);
        return this;
    }


    public ReadConcern getReadConcern() {
        return options.getReadConcern();
    }


    public FindOptions readConcern(final ReadConcern readConcern) {
        options.readConcern(readConcern);
        return this;
    }


    public Collation getCollation() {
        return options.getCollation();
    }


    public FindOptions collation(final Collation collation) {
        options.collation(collation);
        return this;
    }

    DBCollectionFindOptions getOptions() {
        return options;
    }

    boolean isSnapshot() {
        Object snapshot = getModifiers().get("$snapshot");
        return snapshot != null ? (Boolean) snapshot : false;
    }

    boolean hasHint() {
        return getModifiers().get("$indexHint") != null;
    }
}
