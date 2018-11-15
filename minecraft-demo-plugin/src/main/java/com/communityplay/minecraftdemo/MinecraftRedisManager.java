package com.communityplay.minecraftdemo;

import com.tadahtechnologies.redis.RedisManager;
import com.tadahtechnologies.redis.packets.PurchasedPackagePacket;
import org.bukkit.Bukkit;

public class MinecraftRedisManager extends RedisManager {

    public MinecraftRedisManager() {
        super(generatePool(), "minecraft.demo", "minecraft-server", "minecraft-server");

        this.registerPacket(PurchasedPackagePacket.class, new MinecraftRedisListener());
    }

    @Override
    public void sync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(MinecraftDemo.getInstance(), runnable);
    }

    @Override
    public void async(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(MinecraftDemo.getInstance(), runnable);
    }

}
