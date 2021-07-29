package com.example.base.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

/**
 * @author benben
 * @program base
 * @Description 线程池和默认Async注解线程池和定时任务线程池
 * @date 2021-03-24 14:46
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    // implement SchedulingConfigurer
    /*@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 自定义定时任务线程池
        taskRegistrar.setScheduler();
    }*/

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        // 线程池维护线程最小的数量，默认为1
        threadPool.setCorePoolSize(10);
        // 线程池维护线程最大数量，默认为Integer.MAX_VALUE
        threadPool.setMaxPoolSize(30);
        // 阻塞任务队列的大小，默认为Integer.MAX_VALUE，默认使用LinkedBlockingQueue
        threadPool.setQueueCapacity(50);
        // 设置为true的话，keepAliveSeconds参数设置的有效时间对corePoolSize线程也有效，默认是flase
        threadPool.setAllowCoreThreadTimeOut(true);
        // (maxPoolSize-corePoolSize)部分线程空闲最大存活时间，默认存活时间是60s
        threadPool.setKeepAliveSeconds(120);
        // 拒绝策略，当队列workQueue和线程池maxPoolSize都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。
        // 有以下四种策略，当然也可以根据实际业务需求类实现RejectedExecutionHandler接口实现自己的处理策略
        // ①AbortPolicy：丢弃任务，并且抛出RejectedExecutionException异常
        // ②DiscardPolicy：丢弃任务，不处理，不抛出异常
        // ③CallerRunsPolicy：只用调用者所在线程来运行任务
        // ③DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务，并且重复该操作
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // threadFactory：：用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
        threadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("Async-Pool-%d").build());
        threadPool.initialize();
        return threadPool;
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("UploadExcel-Pool-%d").build();
        return new ThreadPoolExecutor(20, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), factory, new ThreadPoolExecutor.AbortPolicy());
    }
}
