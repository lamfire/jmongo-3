package com.lamfire.jmongo.mapping.cache;


public class DefaultEntityCacheFactory implements EntityCacheFactory {


    public EntityCache createCache() {
        return new DefaultEntityCache();
    }
}
