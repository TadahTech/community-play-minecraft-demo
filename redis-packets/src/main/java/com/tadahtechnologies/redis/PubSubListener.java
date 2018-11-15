package com.tadahtechnologies.redis;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 * Listener for all incoming messages sent via redis
 */
public class PubSubListener extends BinaryJedisPubSub {

    private final RedisManager manager;

    public PubSubListener(RedisManager manager) {
        this.manager = manager;
    }

    @Override
    public void onMessage(byte[] chan, byte[] msg) {
        try {
            Packet packet = this.manager.deserializePacket(msg);
            this.manager.handlePacket(packet, packet.getServerSentFrom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
