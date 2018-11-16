package com.communityplay.minecraftdemo;

import com.tadahtechnologies.redis.PacketListener;
import com.tadahtechnologies.redis.models.StreamPackage;
import com.tadahtechnologies.redis.packets.PurchasedPackagePacket;
import org.bukkit.ChatColor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MinecraftRedisListener implements PacketListener<PurchasedPackagePacket> {

    @Override
    public void handlePacket(String serverFrom, PurchasedPackagePacket packet) {
        StreamPackage streamPackage = packet.getStreamPackage();

        MinecraftDemo demo = MinecraftDemo.getInstance();

        switch (streamPackage.getName().toLowerCase()) {
            case "blow up":
                demo.blowUp();
                break;
            case "spawn skeleton":
                demo.spawnSkeleton();
                break;
            case "give me a hand":
                demo.giveHand();
                break;
        }

        String line = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------" +
          ChatColor.RESET + ChatColor.GOLD + " { Community Play } " +
          ChatColor.DARK_AQUA + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------";

        broadcast(line);
        broadcast(" ");
        broadcast(format("Name", streamPackage.getName()));
        broadcast(format("Description", streamPackage.getDescription()));
        broadcast(format("Image URL", streamPackage.getIconUrl()));
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(streamPackage.getCreationDate()), ZoneId.systemDefault());
        broadcast(format("Creation Date", date.toString()));
        broadcast(format("Price", String.valueOf(streamPackage.getPrice())));
        broadcast(format("Playback URL", streamPackage.getPurchasePlaybackUrl()));
        broadcast(" ");
        broadcast(line);

    }

    private void broadcast(String message) {
        MinecraftDemo.getInstance().getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    private String format(String key, String value) {
        return ChatColor.WHITE + key + ": " + ChatColor.GREEN + value;
    }
}
