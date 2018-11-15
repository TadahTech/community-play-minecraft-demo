package com.communityplay.minecraftdemo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftDemo extends JavaPlugin {

    private static MinecraftDemo instance;

    public static MinecraftDemo getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new MinecraftRedisManager();
    }

    @Override
    public void onDisable() {
    }

    public void blowUp() {
        Player player = getServer().getOnlinePlayers().stream().findAny().get();

        for (int i = 0; i < 10; i++) {
            Location location = player.getLocation().clone();
            TNTPrimed tntPrimed = location.getWorld().spawn(location.add(i, 0 ,i), TNTPrimed.class);
            tntPrimed.setFuseTicks(40);
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.09F);
    }

    public void spawnSkeleton() {
        Player player = getServer().getOnlinePlayers().stream().findAny().get();
        Location location = player.getLocation().clone();
        Skeleton skeleton = location.getWorld().spawn(location.add(0, 1 ,0), Skeleton.class);
        skeleton.setCustomName("Community Play");
        skeleton.setCustomNameVisible(true);
    }

    public void giveHand() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName("Community Play Hand");
        itemStack.setItemMeta(meta);

        Player player = getServer().getOnlinePlayers().stream().findAny().get();
        player.getInventory().addItem(itemStack);
    }
}
