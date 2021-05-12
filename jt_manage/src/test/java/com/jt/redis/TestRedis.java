package com.jt.redis;

import com.jt.pojo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.Params;
import redis.clients.jedis.params.SetParams;
@SpringBootTest
public class TestRedis {

    /**
     * 1.redis配置文件3处修改
     * 2.Linux系统防火墙
     * 3.检查redis启动方式 redis-server redis.conf
     * */
    @Test
    public void testSet() throws InterruptedException {
        String host = "192.168.126.129";
        int port = 6379;
        Jedis jedis = new Jedis(host,port);
        //命令怎么敲,代码怎么写
        jedis.set("2011", "redis入门案例");
        System.out.println(jedis.get("2011"));
        jedis.flushAll();
        //测试是否存在
        if(jedis.exists("2011")){
            jedis.del("2011");
        }else{
            jedis.set("num", "100");
            //自增1
            jedis.incr("num");
            jedis.expire("num", 20);
            Thread.sleep(2000);
            //检查超时时间
            System.out.println(jedis.ttl("num"));
            //取消超时时间
            jedis.persist("num");
        }
    }

    //有时我们可能会为数据添加超时时间
    //原子性问题: 有时业务要求要么同时完成,要么同时失败
    @Test
    public void testSetEx(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        //jedis.set("a","aa");
        //服务器异常
        //jedis.expire("a",60);
        jedis.setex("a", 20, "100");
        System.out.println(jedis.get("a"));
    }

    /**
     * 需求1:如果该数据不存在时,才会赋值.
     * 需求2: 如果数据存在时,进行修改, 并且为他设定超时时间 满足原子性要求.
     *
     */
    @Test
    public void testSetNX(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.set("a", "111");
        jedis.setnx("a", "123");
        System.out.println(jedis.get("a"));

        SetParams params = new SetParams();
        params.xx().ex(20);
        jedis.set("b", "bb", params);
    }

    //默认hash是无序的
    @Test
    public void testHash(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.hset("user", "id", "100");
        jedis.hset("user", "name", "101");
        jedis.hset("user", "age", "18");
        System.out.println(jedis.hkeys("user"));

    }

    //队列:先进先出   方向相反
    //栈: 先进后出    方向相同     注意可变参数类型
    @Test
    public void testList(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.lpush("list", "1","2","3","4");
        System.out.println(jedis.rpop("list"));
    }

    @Test
    public void testTx(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        //开启事务
        Transaction transaction = jedis.multi();
        try {
            transaction.set("a", "a");
            transaction.set("b", "b");
            transaction.set("b", "b");
            //提交事务
            transaction.exec();
        }catch (Exception e){
            transaction.discard();
        }
    }

    @Autowired
    private Jedis jedis;

    @Test
    public void testSpringBootRedis(){
        jedis.set("abc", "abc");
        System.out.println(jedis.get("abc"));

    }





}
