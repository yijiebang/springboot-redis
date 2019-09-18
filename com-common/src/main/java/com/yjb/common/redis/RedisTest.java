package com.yjb.common.redis;

import redis.clients.jedis.Jedis;

/**
 * 測試redis是否開啓,簡單的操作
 * cmd:C:\Users\xxxxx>redis-cli.exe -h 127.0.0.1 -p 6379 --raw
 */
public class RedisTest {

    public static void main(String[] args) {
        //连接Redis   设置ip和端口
        Jedis redis = new Jedis("127.0.0.1",6379);
        System.out.println("redis connect success .");
        // 设置Redis链接密码，無密碼可注釋
        redis.auth("123456");
        //查看服务是否运行
        System.out.println("服务正在运行: "+redis.ping());// PONG 表示正确

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

        redis.save();
        // 关闭链接
        redis.close();
    }
}
