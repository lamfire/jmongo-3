

package com.lamfire.jmongo;

import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.QueryException;
import com.lamfire.jmongo.utils.Assert;
import com.mongodb.DBCollection;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Collation;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MapReduceOptions<T> {
    private String outputDB;
    private String outputCollection;
    private String inputCollection;
    private String map;
    private String reduce;
    private OutputType outputType;
    private Query query;
    private String finalize;
    private ReadPreference readPreference;
    private int limit;
    private long maxTimeMS;
    private Map<String, Object> scope;
    private boolean jsMode;
    private boolean verbose;
    private boolean bypassDocumentValidation;
    private Collation collation;
    private Class<T> resultType;


    public MapReduceOptions<T> bypassDocumentValidation(final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }


    public MapReduceOptions<T> collation(final Collation collation) {
        this.collation = collation;
        return this;
    }


    public MapReduceOptions<T> finalize(final String finalize) {
        this.finalize = finalize;
        return this;
    }


    public MapReduceOptions<T> jsMode(final Boolean jsMode) {
        this.jsMode = jsMode;
        return this;
    }


    public MapReduceOptions<T> limit(final int limit) {
        this.limit = limit;
        return this;
    }


    public MapReduceOptions<T> map(final String map) {
        Assert.parametersNotNull("map", map);
        Assert.parameterNotEmpty("map", map);
        this.map = map;
        return this;
    }


    public MapReduceOptions<T> maxTimeMS(final long maxTimeMS) {
        this.maxTimeMS = maxTimeMS;
        return this;
    }


    public MapReduceOptions<T> inputCollection(final String name) {
        this.inputCollection = name;
        return this;
    }


    public MapReduceOptions<T> outputCollection(final String name) {
        this.outputCollection = name;
        return this;
    }


    public MapReduceOptions<T> outputDB(final String outputDB) {
        this.outputDB = outputDB;
        return this;
    }


    public MapReduceOptions<T> outputType(final OutputType outputType) {
        this.outputType = outputType;
        return this;
    }


    public MapReduceOptions<T> query(final Query query) {
        Assert.parametersNotNull("query", query);
        this.query = query;
        return this;
    }


    public MapReduceOptions<T> readPreference(final ReadPreference preference) {
        this.readPreference = preference;
        return this;
    }


    public MapReduceOptions<T> reduce(final String reduce) {
        Assert.parametersNotNull("reduce", reduce);
        Assert.parameterNotEmpty("reduce", reduce);
        this.reduce = reduce;
        return this;
    }


    public MapReduceOptions<T> resultType(final Class<T> resultType) {
        this.resultType = resultType;
        return this;
    }


    public MapReduceOptions<T> scope(final Map<String, Object> scope) {
        this.scope = scope;
        return this;
    }


    public MapReduceOptions<T> verbose(final Boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    OutputType getOutputType() {
        return outputType;
    }

    Query getQuery() {
        return query;
    }

    Class<T> getResultType() {
        return resultType;
    }

    @SuppressWarnings("deprecation")
    MapReduceCommand toCommand(final Mapper mapper) {
        if (query.getOffset() != 0 || query.getFieldsObject() != null) {
            throw new QueryException("mapReduce does not allow the offset/retrievedFields query ");
        }

        final DBCollection dbColl = inputCollection != null ? getQuery().getCollection().getDB().getCollection(inputCollection)
                                                            : query.getCollection();
        final String target = outputCollection != null ? outputCollection : mapper.getMappedClass(resultType).getCollectionName();

        final MapReduceCommand command = new MapReduceCommand(dbColl, map, reduce, target, outputType, query.getQueryObject());
        command.setBypassDocumentValidation(bypassDocumentValidation);
        command.setCollation(collation);
        command.setFinalize(finalize);
        command.setJsMode(jsMode);
        command.setLimit(limit);
        command.setMaxTime(maxTimeMS, TimeUnit.MILLISECONDS);
        command.setOutputDB(outputDB);
        command.setReadPreference(readPreference);
        command.setScope(scope);
        command.setSort(query.getSortObject());
        command.setVerbose(verbose);

        return command;
    }
}
