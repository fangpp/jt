package com.jt.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRedis2 {

    /**
     * 3台redes  key如何存储??  79/80/81
     */
    @Test
    public void testShards(){
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.126.129", 6379));
        shards.add(new JedisShardInfo("192.168.126.129", 6380));
        shards.add(new JedisShardInfo("192.168.126.129", 6381));
        ShardedJedis shardedJedis = new ShardedJedis(shards);
        shardedJedis.set("shards", "redis分片机制");
        System.out.println(shardedJedis.get("shards"));
    }

    /**
     * 测试哨兵API
     */
    @Test
    public void testSentinel(){
        Set<String> sets = new HashSet<>();
        sets.add("192.168.126.129:26379");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(10);
        poolConfig.setMaxIdle(40);
        poolConfig.setMaxTotal(1000);
        JedisSentinelPool pool = new JedisSentinelPool("mymaster",sets,poolConfig);
        Jedis jedis = pool.getResource();
        jedis.set("AAA", "您好Redis");
        System.out.println(jedis.get("AAA"));
        jedis.close();
    }

    /**
     * Redis集群测试
     *
     * set 操作主机!!   slave 只负责数据的同步
     */
    @Test
    public void testRedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.126.129", 7000));
        nodes.add(new HostAndPort("192.168.126.129", 7001));
        nodes.add(new HostAndPort("192.168.126.129", 7002));
        nodes.add(new HostAndPort("192.168.126.129", 7003));
        nodes.add(new HostAndPort("192.168.126.129", 7004));
        nodes.add(new HostAndPort("192.168.126.129", 7005));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("aa", "redis集群测试"); //???如何存储?
        System.out.println("获取数据:"+jedisCluster.get("aa"));
    }
}
