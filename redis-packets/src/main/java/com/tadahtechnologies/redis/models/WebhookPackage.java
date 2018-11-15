package com.tadahtechnologies.redis.models;

public class WebhookPackage {

    private String secret;
    private PurchasedPackage purchasedPackage;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public PurchasedPackage getPurchasedPackage() {
        return purchasedPackage;
    }

    public void setPurchasedPackage(PurchasedPackage purchasedPackage) {
        this.purchasedPackage = purchasedPackage;
    }
}
