package com.lamfire.jmongo.mapping.cache;



public class EntityCacheStatistics {
    private int entities;
    private int hits;
    private int misses;


    public EntityCacheStatistics copy() {
        final EntityCacheStatistics copy = new EntityCacheStatistics();
        copy.entities = entities;
        copy.hits = hits;
        copy.misses = misses;
        return copy;
    }


    public void incEntities() {
        entities++;
    }


    public void incHits() {
        hits++;
    }


    public void incMisses() {
        misses++;
    }


    public void reset() {
        entities = 0;
        hits = 0;
        misses = 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + entities + " entities, " + hits + " hits, " + misses + " misses.";
    }
}
