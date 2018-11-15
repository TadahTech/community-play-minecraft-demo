package com.communityplay.minecraftdemo.spring;

import com.tadahtechnologies.redis.models.StreamPackage;
import com.tadahtechnologies.redis.models.WebhookPackage;
import com.tadahtechnologies.redis.packets.PurchasedPackagePacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("minecraftDemo")
public class WebhookController {

    private final SpringRedisManager redisManager;

    @Autowired
    public WebhookController(SpringRedisManager redisManager) {
        this.redisManager = redisManager;
    }

    @PostMapping
    public ResponseEntity postPackage(@RequestBody WebhookPackage webhookPackage) {
        StreamPackage streamPackage = webhookPackage.getPurchasedPackage().getStreamPackage();
        String secret = webhookPackage.getSecret();

        if (!secret.equalsIgnoreCase("98ee21a9de10e5a68b53a37788f738ed1169b49020d9879ea7f6fb12ee1e4ce3")) {
            return new ResponseEntity<>("Unknonw origin secret", HttpStatus.BAD_REQUEST);
        }

        switch (streamPackage.getName().toLowerCase()) {
            case "Blow Me Up":
                break;
            case "Spawn Skeleton":
                break;
            case "Give Me a Hand":
                break;
            default:
                break;
        }

        PurchasedPackagePacket packet = new PurchasedPackagePacket();
        packet.setStreamPackage(streamPackage);

        redisManager.sendPacket(packet, null);

        return new ResponseEntity<>("Received", HttpStatus.OK);
    }

}
