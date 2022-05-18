package com.lamfire.jmongo;


import com.lamfire.jmongo.annotations.NotSaved;
import com.lamfire.jmongo.annotations.Transient;
import com.lamfire.jmongo.logging.JmongoLoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.mapping.MappingException;
import com.lamfire.jmongo.mapping.cache.EntityCache;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.query.QueryIterator;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;

import java.util.Iterator;


@NotSaved
@SuppressWarnings("deprecation")
public class MapreduceResults<T> implements Iterable<T> {
    private static final Logger LOG = JmongoLoggerFactory.get(MapreduceResults.class);
    private final Stats counts = new Stats();
    private MapReduceOutput output;
    private String outputCollectionName;
    private OutputType outputType;
    private Query<T> query;

    @Transient
    private Class<T> clazz;
    @Transient
    private Mapper mapper;
    @Transient
    private EntityCache cache;
    private DataStore datastore;


    public MapreduceResults(final MapReduceOutput output) {
        this.output = output;
        outputCollectionName = output.getCollectionName();
    }


    public Query<T> createQuery() {
        if (outputType == OutputType.INLINE) {
            throw new MappingException("No collection available for inline mapreduce jobs");
        }
        return query.cloneQuery();
    }


    public Stats getCounts() {
        return counts;
    }


    public long getElapsedMillis() {
        return output.getDuration();
    }


    @Deprecated
    public String getError() {
        LOG.warn("MapreduceResults.getError() will always return null.");
        return null;
    }


    public Iterator<T> getInlineResults() {
        return new QueryIterator<T, T>(datastore, output.results().iterator(), mapper, clazz, null, cache);
    }


    @Deprecated
    public MapreduceType getType() {
        if (outputType == OutputType.REDUCE) {
            return MapreduceType.REDUCE;
        } else if (outputType == OutputType.MERGE) {
            return MapreduceType.MERGE;
        } else if (outputType == OutputType.INLINE) {
            return MapreduceType.INLINE;
        } else {
            return MapreduceType.REPLACE;
        }

    }

    @Deprecated
    void setType(final MapreduceType type) {
        this.outputType = type.toOutputType();
    }


    public OutputType getOutputType() {
        return outputType;
    }


    public void setOutputType(final OutputType outputType) {
        this.outputType = outputType;
    }


    @Deprecated
    public boolean isOk() {
        LOG.warn("MapreduceResults.isOk() will always return true.");
        return true;
    }


    @Override
    public Iterator<T> iterator() {
        return outputType == OutputType.INLINE ? getInlineResults() : createQuery().fetch().iterator();
    }


    public void setInlineRequiredOptions(final DataStore datastore, final Class<T> clazz, final Mapper mapper, final EntityCache cache) {
        this.mapper = mapper;
        this.datastore = datastore;
        this.clazz = clazz;
        this.cache = cache;
    }


    public class Stats {

        public int getEmitCount() {
            return output.getEmitCount();
        }


        public int getInputCount() {
            return output.getInputCount();
        }


        public int getOutputCount() {
            return output.getOutputCount();
        }
    }

    String getOutputCollectionName() {
        return outputCollectionName;
    }

    void setQuery(final Query<T> query) {
        this.query = query;
    }
}
