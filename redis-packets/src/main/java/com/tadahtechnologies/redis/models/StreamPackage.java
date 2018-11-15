package com.tadahtechnologies.redis.models;

public class StreamPackage {

    private String name;
    private String description;
    private String iconUrl;
    private String purchasePlaybackUrl;
    private double price;

    //Meta
    private int gridPos;
    private long creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPurchasePlaybackUrl() {
        return purchasePlaybackUrl;
    }

    public void setPurchasePlaybackUrl(String purchasePlaybackUrl) {
        this.purchasePlaybackUrl = purchasePlaybackUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGridPos() {
        return gridPos;
    }

    public void setGridPos(int gridPos) {
        this.gridPos = gridPos;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
