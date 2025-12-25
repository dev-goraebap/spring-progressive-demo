package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockedIpViewModel {
    private Long id;
    private String ipAddress;
    private String reason;
    private String blockedBy;
    private LocalDateTime expiresAt;
    private String isActiveYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        if (!"Y".equals(isActiveYn)) {
            return false;
        }
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isPermanent() {
        return expiresAt == null;
    }

    public String getBlockedByLabel() {
        return "auto".equals(blockedBy) ? "자동" : "수동";
    }

    public String getStatusLabel() {
        if (!isActive()) {
            return "해제됨";
        }
        if (isExpired()) {
            return "만료됨";
        }
        return "차단중";
    }
}
