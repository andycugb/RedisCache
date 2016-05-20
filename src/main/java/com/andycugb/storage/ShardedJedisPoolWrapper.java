package com.andycugb.storage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author tangyang
 *
 */
public class ShardedJedisPoolWrapper {

    public static final int REDIS_TIMEOUT = 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardedJedisPoolWrapper.class);
    private ShardedJedisPool jedisPool;

    public ShardedJedisPoolWrapper(JedisPoolConfig config, String address) {
        LOGGER.info("Redis started on address: " + address);
        jedisPool = new ShardedJedisPool(config, buildShardInfos(address, ""));
    }

    public ShardedJedisPoolWrapper(JedisPoolConfig config, String address, String password) {
        LOGGER.info("Redis started on address: " + address);
        jedisPool = new ShardedJedisPool(config, buildShardInfos(address, password));
    }

    private static List<JedisShardInfo> buildShardInfos(String address, String password) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        for (String addr : address.split(" ")) {
            String[] parts = addr.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);
            JedisShardInfo info = new JedisShardInfo(host, port, REDIS_TIMEOUT);
            if (!StringUtils.isEmpty(password)) {
                info.setPassword(password);
            }
            shards.add(info);
        }
        return shards;
    }

    public ShardedJedisPool getJedisPool() {
        return this.jedisPool;
    }
}
