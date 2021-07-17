package com.example.base;

import com.example.base.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.base.dao"})
@EnableAsync // SimpleAsyncTaskExecutor 每个任务创建一个线程
@Slf4j
public class BaseApplication implements CommandLineRunner, ApplicationRunner {

    @Resource
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }

    /**
     * order 2 后启动
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // 默认scheduling线程，会占用定时任务的线程（默认定时任务是单线程）
        // 使用implements AsyncConfigurer自定义线程池
        log.info("CommandLineRunner执行");
        // nettyServer.start();
    }

    /**
     * order 1 先启动
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationRunner执行");
    }

    /**
     * war打包
     * extends SpringBootServletInitializer
     */
    /*
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BaseApplication.class);
    }
    */

}
