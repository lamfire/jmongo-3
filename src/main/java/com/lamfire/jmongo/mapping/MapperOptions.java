package com.lamfire.jmongo.mapping;


import com.lamfire.jmongo.ObjectFactory;
import com.lamfire.jmongo.logging.LoggerFactory;
import com.lamfire.jmongo.logging.Logger;
import com.lamfire.jmongo.mapping.cache.DefaultEntityCacheFactory;
import com.lamfire.jmongo.mapping.cache.EntityCacheFactory;


@SuppressWarnings("deprecation")
public class MapperOptions {
    private static final Logger LOG = LoggerFactory.get(MapperOptions.class);

    @Deprecated
    private boolean actLikeSerializer;
    private boolean ignoreFinals; //ignore final fields.
    private boolean storeNulls;
    private boolean storeEmpties;
    private boolean useLowerCaseCollectionNames;
    private boolean cacheClassLookups = false;
    private boolean mapSubPackages = false;
    private ObjectFactory objectFactory = new DefaultCreator(this);
    private EntityCacheFactory cacheFactory = new DefaultEntityCacheFactory();
    private CustomMapper embeddedMapper = new EmbeddedMapper();
    private CustomMapper defaultMapper = embeddedMapper;
    private CustomMapper referenceMapper = new ReferenceMapper();
    private CustomMapper valueMapper = new ValueMapper();
    private com.lamfire.jmongo.mapping.lazy.DatastoreProvider datastoreProvider = null;


    public MapperOptions() {
    }


    public MapperOptions(final MapperOptions options) {
        setActLikeSerializer(options.isActLikeSerializer());
        setIgnoreFinals(options.isIgnoreFinals());
        setStoreNulls(options.isStoreNulls());
        setStoreEmpties(options.isStoreEmpties());
        setUseLowerCaseCollectionNames(options.isUseLowerCaseCollectionNames());
        setCacheClassLookups(options.isCacheClassLookups());
        setObjectFactory(options.getObjectFactory());
        setCacheFactory(options.getCacheFactory());
        setEmbeddedMapper(options.getEmbeddedMapper());
        setDefaultMapper(options.getDefaultMapper());
        setReferenceMapper(options.getReferenceMapper());
        setValueMapper(options.getValueMapper());
    }


    public EntityCacheFactory getCacheFactory() {
        return cacheFactory;
    }


    public void setCacheFactory(final EntityCacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }


    @Deprecated
    public com.lamfire.jmongo.mapping.lazy.DatastoreProvider getDatastoreProvider() {
        return datastoreProvider;
    }


    @Deprecated
    public void setDatastoreProvider(final com.lamfire.jmongo.mapping.lazy.DatastoreProvider datastoreProvider) {
        LOG.warning("DatastoreProviders are no longer needed or used.");
        this.datastoreProvider = datastoreProvider;
    }


    public CustomMapper getDefaultMapper() {
        return defaultMapper;
    }


    public void setDefaultMapper(final CustomMapper pDefaultMapper) {
        defaultMapper = pDefaultMapper;
    }


    public CustomMapper getEmbeddedMapper() {
        return embeddedMapper;
    }


    public void setEmbeddedMapper(final CustomMapper pEmbeddedMapper) {
        embeddedMapper = pEmbeddedMapper;
    }


    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }


    public void setObjectFactory(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }


    public CustomMapper getReferenceMapper() {
        return referenceMapper;
    }


    public void setReferenceMapper(final CustomMapper pReferenceMapper) {
        referenceMapper = pReferenceMapper;
    }


    public CustomMapper getValueMapper() {
        return valueMapper;
    }


    public void setValueMapper(final CustomMapper pValueMapper) {
        valueMapper = pValueMapper;
    }


    @Deprecated
    public boolean isActLikeSerializer() {
        return actLikeSerializer;
    }


    @Deprecated
    public void setActLikeSerializer(final boolean actLikeSerializer) {
        this.actLikeSerializer = actLikeSerializer;
    }


    public boolean isCacheClassLookups() {
        return cacheClassLookups;
    }


    public void setCacheClassLookups(final boolean cacheClassLookups) {
        this.cacheClassLookups = cacheClassLookups;
    }


    public boolean isIgnoreFinals() {
        return ignoreFinals;
    }


    public void setIgnoreFinals(final boolean ignoreFinals) {
        this.ignoreFinals = ignoreFinals;
    }


    public boolean isStoreEmpties() {
        return storeEmpties;
    }


    public void setStoreEmpties(final boolean storeEmpties) {
        this.storeEmpties = storeEmpties;
    }


    public boolean isStoreNulls() {
        return storeNulls;
    }


    public void setStoreNulls(final boolean storeNulls) {
        this.storeNulls = storeNulls;
    }


    public boolean isUseLowerCaseCollectionNames() {
        return useLowerCaseCollectionNames;
    }


    public void setUseLowerCaseCollectionNames(final boolean useLowerCaseCollectionNames) {
        this.useLowerCaseCollectionNames = useLowerCaseCollectionNames;
    }


    public boolean isMapSubPackages() {
        return mapSubPackages;
    }


    public void setMapSubPackages(final boolean mapSubPackages) {
        this.mapSubPackages = mapSubPackages;
    }
}
