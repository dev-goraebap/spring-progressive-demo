package xyz.goraebap.blog.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fcm_subscriptions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(length = 50)
    private String browser;

    @Column(length = 50)
    private String os;

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static FcmSubscriptionEntity create(String token, String ipAddress, String browser, String os, String deviceType) {
        FcmSubscriptionEntity entity = new FcmSubscriptionEntity();
        entity.token = token;
        entity.ipAddress = ipAddress;
        entity.browser = browser;
        entity.os = os;
        entity.deviceType = deviceType;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}