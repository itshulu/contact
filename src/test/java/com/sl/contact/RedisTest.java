package com.sl.contact;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisTest {

    private Jedis jedis;

    @Before
    public void before() {
        jedis = new Jedis("localhost");
    }

    @After
    public void after() {
        jedis.close();
    }

    @Test
    public void test() {
        Set<String> keys = jedis.keys("contact:*");
        keys.forEach(System.out::println);
        System.out.println();
        jedis.hgetAll("contact:2bf5ec7e7e6e4e4fb986b183b41fa9e0").forEach((k, v) -> {
            System.out.println(k + "--" + v);
        });
        jedis.hset("contact:2bf5ec7e7e6e4e4fb986b183b41fa9e0","name","1111");
        jedis.hgetAll("contact:2bf5ec7e7e6e4e4fb986b183b41fa9e0").forEach((k, v) -> {
            System.out.println(k + "--" + v);
        });

    }
}
