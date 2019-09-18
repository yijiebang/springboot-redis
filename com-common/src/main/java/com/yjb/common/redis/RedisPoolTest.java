package com.yjb.common.redis;

import redis.clients.jedis.Jedis;

public class RedisPoolTest {
    public static void main(String[] args) {
        Jedis redis=RedisUtil.getJedis();//获取连接
        /**对String类型进行操作*/
        // 创建一个字符串myset
        redis.set("myset", "张三");
        // 追加字符
        redis.append("myset", "是个学生");
        // 输出这个字符串
        System.out.println(redis.get("myset"));


        // 同时设置多个String
        redis.mset("name", "小强","password","123" ,"note","备注");

        // 创建一个age的字符串
        redis.set("age", "10");
        System.out.println(redis.get("age"));
        //将age 增加1
        redis.incr("age");
        System.out.println(redis.get("age"));
        // 关闭链接
        RedisUtil.close(redis);
    }
}
