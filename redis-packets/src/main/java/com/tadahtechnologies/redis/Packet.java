package com.tadahtechnologies.redis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * An abstract class representing a server <-> server message and contents.
 */
public abstract class Packet {

    private final String name;

    private long sentAt;
    private String serverSentFrom;
    private String serverTarget;

    public Packet() {
        this.name = getClass().getSimpleName().replace("Packet", "");
    }

    public void preWrite(DataOutputStream out) throws IOException {
        out.writeLong(System.currentTimeMillis());
        out.writeUTF(serverSentFrom);
        out.writeUTF(serverTarget);
    }

    public void preRead(DataInputStream in) throws IOException {
        this.sentAt = in.readLong();
        this.serverSentFrom = in.readUTF();
        this.serverTarget = in.readUTF();
    }

    public abstract void read(DataInputStream in) throws IOException;

    public abstract void write(DataOutputStream out) throws IOException;

    public boolean runOnSameServer() {
        return false;
    }

    public boolean sendOnMainThread() {
        return false;
    }

    public String getName() {
        return name;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt() {
        this.sentAt = System.currentTimeMillis();
    }

    public String getServerSentFrom() {
        return serverSentFrom;
    }

    public void setServerSentFrom(String serverSentFrom) {
        this.serverSentFrom = serverSentFrom;
    }

    public String getServerTarget() {
        return serverTarget;
    }

    public void setServerTarget(String serverTarget) {
        this.serverTarget = serverTarget;
    }

    public void onExpires() {
        //Leave empty
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Packet)) {
            return false;
        }

        Packet packet = (Packet) o;
        return sentAt == packet.sentAt &&
          Objects.equals(name, packet.name) &&
          Objects.equals(serverSentFrom, packet.serverSentFrom) &&
          Objects.equals(serverTarget, packet.serverTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sentAt, serverSentFrom, serverTarget);
    }

    @Override
    public String toString() {
        return "Packet{" +
          "name='" + name + '\'' +
          ", sentAt=" + sentAt +
          ", serverSentFrom='" + serverSentFrom + '\'' +
          ", serverTarget='" + serverTarget + '\'' +
          '}';
    }
}
