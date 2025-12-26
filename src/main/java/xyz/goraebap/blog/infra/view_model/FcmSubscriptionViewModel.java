package xyz.goraebap.blog.infra.view_model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FcmSubscriptionViewModel {
    private Long id;
    private String token;
    private String ipAddress;
    private String browser;
    private String os;
    private String deviceType;
    private LocalDateTime createdAt;

    public String getTokenPreview() {
        if (token == null || token.length() <= 20) {
            return token;
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }

    public String getDeviceTypeLabel() {
        if (deviceType == null) return "-";
        return switch (deviceType.toLowerCase()) {
            case "desktop" -> "데스크탑";
            case "mobile" -> "모바일";
            case "tablet" -> "태블릿";
            default -> deviceType;
        };
    }
}
