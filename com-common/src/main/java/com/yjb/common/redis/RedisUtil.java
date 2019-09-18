package com.yjb.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**cmd启动服务：  redis-server.exe
 *cmd启动客户端： redis-cli.exe -h 127.0.0.1 -p 6379 --raw
 * 设置密码：config set requirepass "123456"
 * redis工具类,默认redis是无用户和密码，
 * Redis默认支持16个数据库，这些数据库的默认命名都是从0开始递增的数字。
 * 当我们连接Redis服务时，默认操作的是0号数据库，可以通过SELECT命令更换数据库：
 */
public class RedisUtil {
    private RedisUtil() {}

    private static String ip = "127.0.0.1";
    private static int port = 6379;
    private static int timeout = 10000;
    private static String auth = "123456";

    private static JedisPool pool  = null ;
    static {
        /***初始化连接池*/
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大链接数
        config.setMaxTotal(1024);
        // 设置最大空闲实例数
        config.setMaxIdle(200);
        // 设置连接池最大等待时间, 毫秒(如果是-1 表示永不超时,会一直等)
        config.setMaxWaitMillis(10000);
        // borrow一个实例的时候,是否提前进行validate操作
        config.setTestOnBorrow(true);

        // 创建链接池
        pool = new JedisPool(config,ip,port,timeout,auth);
    }
    // 得到Redis链接 (同步的)
    public synchronized static Jedis getJedis() {
        if(pool != null) {
            return pool.getResource(); // 获取链接
        }
        return null;
    }

    // 关闭redis链接
    public static void close (final Jedis redis) {
        if(redis != null) {
            redis.close();
        }
    }

}
