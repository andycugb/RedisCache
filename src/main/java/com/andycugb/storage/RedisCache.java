package com.andycugb.storage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author andycugb persistenceJedisPool
 */
public abstract class RedisCache extends AbstractRedisCache {

    @Resource(name = "persistenceJedisPool")
    protected ShardedJedisPoolWrapper shardedJedisPoolWrapper;

    @PostConstruct
    public void init() {
        this.pool = shardedJedisPoolWrapper.getJedisPool();
    }
}
