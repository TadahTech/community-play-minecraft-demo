package com.communityplay.minecraftdemo.spring;

import com.tadahtechnologies.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
public class SpringRedisManager extends RedisManager {

    private ExecutorService executorService;

    @Autowired
    public SpringRedisManager() {
        super(generatePool(), "minecraft.demo", "spring-server", "spring-server");

        this.executorService = new ScheduledThreadPoolExecutor(100);
    }

    @Override
    public void sync(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void async(Runnable runnable) {
        this.executorService.submit(runnable);
    }
}
