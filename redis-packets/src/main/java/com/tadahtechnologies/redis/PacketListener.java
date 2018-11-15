package com.tadahtechnologies.redis;

/**
 * Handles all incoming packets and how to respond to them
 */
public interface PacketListener<T extends Packet> {

    void handlePacket(String serverFrom, T packet);
}
