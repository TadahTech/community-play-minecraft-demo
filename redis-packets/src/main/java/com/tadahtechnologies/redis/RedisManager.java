package com.tadahtechnologies.redis;

import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Handles all things redis related
 */
public abstract class RedisManager {

    private final Map<Class<? extends Packet>, PacketListener<? extends Packet>> packetListeners = Maps.newConcurrentMap();
    private final String localChannel;
    private final String globalChannel;
    private final JedisPool pool;
    private final String serverInternalName, serverExternalName;

    public RedisManager(JedisPool pool, String channel, String serverInternalName, String serverExternalName) {
        this(pool, channel, true, serverInternalName, serverExternalName);
    }

    public RedisManager(JedisPool pool, String channel, boolean subscribe, String serverInternalName, String serverExternalName) {
        this.pool = pool;

        this.serverInternalName = serverInternalName;
        this.serverExternalName = serverExternalName;
        if (channel.contains("-")) {
            throw new IllegalArgumentException("Channel may not contain \"-\"");
        }

        this.localChannel = channel + "-PubSub";
        this.globalChannel = channel + "-PubSub-Global";

        if (subscribe) {
            subscribe();
        }
    }

    public static JedisPool generatePool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMinIdle(5);

        jedisPoolConfig.setMaxTotal(20);

        return new JedisPool(jedisPoolConfig, "localhost", 6379, 2000);
    }

    protected void subscribe() {
        async(() -> {
            byte[] channelGlobal = serialize(globalChannel);
            byte[] channelLocal = serialize(localChannel + "-" + serverInternalName);

            try (Jedis jedis = this.pool.getResource()) {
                jedis.subscribe(new PubSubListener(this), channelGlobal, channelLocal);
            }
        });
    }

    public String getServerExternalName() {
        return serverExternalName;
    }

    public String getServerInternalName() {
        return serverInternalName;
    }

    public <T extends Packet> void registerPacket(Class<T> clazz, PacketListener<T> listener) {
        packetListeners.put(clazz, listener);
    }

    public void sendPacket(Packet packet, String server) {
        boolean sendToAll = server == null;

        packet.setServerSentFrom(getServerInternalName());
        packet.setServerTarget(sendToAll ? "GLOBAL" : server);

        if (packet.sendOnMainThread()) {
            try (Jedis jedis = pool.getResource()) {
                jedis.publish(sendToAll ? serialize(globalChannel) : serialize(localChannel + "-" + packet.getServerTarget()), serializePacket(packet));
            }
            return;
        }

        async(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.publish(sendToAll ? serialize(globalChannel) : serialize(localChannel + "-" + packet.getServerTarget()), serializePacket(packet));
            }
        });
    }

    public void handlePacket(Packet packet, String serverFrom) {
        if (packet.getServerSentFrom().equalsIgnoreCase(getServerInternalName()) && !packet.runOnSameServer()) {
            return;
        }

        PacketListener listener = packetListeners.get(packet.getClass());
        if (listener == null) {
            throw new IllegalArgumentException("No listener found for the packet: " + packet.getName() + ", sent from " + serverFrom);
        }

        sync(() -> listener.handlePacket(serverFrom, packet));
    }

    public byte[] serialize(String name) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bOut);

        try {
            out.writeUTF(name);
            return bOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Ideally this should never happen
    }

    public String deserialize(byte[] src) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(src));

        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] serializePacket(Packet packet) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bOut);

        try {
            out.writeUTF(packet.getClass().getName());
            packet.preWrite(out);
            packet.write(out);
            return bOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Ideally this should never happen
    }

    public Packet deserializePacket(byte[] src) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(src));
        Packet packet;

        try {
            String className = in.readUTF();
            Class<?> clazz = Class.forName(className);
            packet = (Packet) clazz.newInstance();
            packet.preRead(in);
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

        try {
            packet.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public abstract void sync(Runnable runnable);

    public abstract void async(Runnable runnable);

    public JedisPool getPool() {
        return pool;
    }
}
