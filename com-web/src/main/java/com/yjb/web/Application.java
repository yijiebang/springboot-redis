package com.yjb.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/****
 * 由于Mapper层访问数据库了，但是没有注入，所以指定扫描这个包下所有用了mybatis的注解的接口，
 * @MapperScan注解只会扫描包中的接口  :@MapperScan(basePackages = {"com.yjb.*"})
 * 由于启动类不是放在src下面，所以要指定扫描的包才行 :@SpringBootApplication(scanBasePackages = "com.yjb")
 * 使用lombok的 @Slf4j 注解，不需要创建LOGGER对象 :@Slf4j
 *@EnableRedisHttpSession 来开启spring session支持
 * *****/
@MapperScan(basePackages = {"com.yjb.web.mapper"})
@SpringBootApplication(scanBasePackages = "com.yjb")
@Slf4j
public class Application {

    public static void main(String[] args) {
         ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        log.info("服务启动成功！服务路径[{}],服务端口[{}]", applicationContext.getEnvironment().getProperty("server.servlet.context-path"),
                applicationContext.getEnvironment().getProperty("server.port"));
    }
}
