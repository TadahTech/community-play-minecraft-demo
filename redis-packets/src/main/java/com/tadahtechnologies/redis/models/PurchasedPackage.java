package com.tadahtechnologies.redis.models;

import java.util.UUID;

public class PurchasedPackage {

    private Long id;
    private UUID streamerId;
    private String purchased;
    private String shop;
    private String purchaser;
    private double cost;
    private long purchasedAt;
    private StreamPackage streamPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(UUID streamerId) {
        this.streamerId = streamerId;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(long purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public StreamPackage getStreamPackage() {
        return streamPackage;
    }

    public void setStreamPackage(StreamPackage streamPackage) {
        this.streamPackage = streamPackage;
    }
}
