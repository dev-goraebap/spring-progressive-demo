package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_ips")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedIpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false, unique = true, columnDefinition = "inet")
    private String ipAddress;

    @Column(columnDefinition = "text")
    private String reason;

    @Column(name = "blocked_by", nullable = false, length = 20)
    private String blockedBy = "manual";

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active_yn", nullable = false, length = 1)
    private String isActiveYn = "Y";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 수동 차단 생성 (영구)
     */
    public static BlockedIpEntity createManual(String ipAddress, String reason) {
        BlockedIpEntity entity = new BlockedIpEntity();
        entity.ipAddress = ipAddress;
        entity.reason = reason;
        entity.blockedBy = "manual";
        entity.expiresAt = null;
        entity.isActiveYn = "Y";
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    /**
     * 자동 차단 생성 (임시)
     */
    public static BlockedIpEntity createAuto(String ipAddress, String reason, int hours) {
        BlockedIpEntity entity = new BlockedIpEntity();
        entity.ipAddress = ipAddress;
        entity.reason = reason;
        entity.blockedBy = "auto";
        entity.expiresAt = LocalDateTime.now().plusHours(hours);
        entity.isActiveYn = "Y";
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return entity;
    }

    /**
     * 차단 해제
     */
    public void deactivate() {
        this.isActiveYn = "N";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 영구 차단으로 변경
     */
    public void makePermanent() {
        this.expiresAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 활성 상태인지 확인
     */
    public boolean isActive() {
        if (!"Y".equals(isActiveYn)) {
            return false;
        }
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    /**
     * 만료되었는지 확인
     */
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}
