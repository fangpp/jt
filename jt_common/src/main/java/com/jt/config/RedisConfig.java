package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration  //标识为一个配置类, 一般整合第三方
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.nodes}")
    private String nodes;       //node,node,node

    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodeSet = new HashSet<>();
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray){
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(host,port);
            nodeSet.add(hostAndPort);
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxIdle(40);
        jedisPoolConfig.setMaxTotal(1000);
        return new JedisCluster(nodeSet,jedisPoolConfig);
    }










   /* @Bean
    public ShardedJedis shardedJedis(){
        List<JedisShardInfo> shards = new ArrayList<>();
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray){ //node=host:port
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            JedisShardInfo info = new JedisShardInfo(host, port);
            shards.add(info);
        }
        return new ShardedJedis(shards);
        //2.编辑redis配置文件,调整链接数量
        *//*JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(10); //最小空闲数量
        jedisPoolConfig.setMaxIdle(40); //最大的空闲数量
        jedisPoolConfig.setMaxTotal(1000);
        ShardedJedisPool shardedJedisPool =
                new ShardedJedisPool(jedisPoolConfig, shards);
        return shardedJedisPool.getResource();*//*
    }*/



   /* @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private Integer port;

    @Bean   //将该方法的返回值,交给Spring容器管理
    public Jedis jedis(){

        return new Jedis(host,port);
    }*/
}
