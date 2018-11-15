package com.tadahtechnologies.redis.packets;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tadahtechnologies.redis.Packet;
import com.tadahtechnologies.redis.models.StreamPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PurchasedPackagePacket extends Packet {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private StreamPackage streamPackage;

    @Override
    public void read(DataInputStream in) throws IOException {
        try {
            this.setStreamPackage(MAPPER.readValue(in.readUTF(), StreamPackage.class));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(MAPPER.writeValueAsString(this.streamPackage));
    }

    public StreamPackage getStreamPackage() {
        return streamPackage;
    }

    public void setStreamPackage(StreamPackage streamPackage) {
        this.streamPackage = streamPackage;
    }
}
