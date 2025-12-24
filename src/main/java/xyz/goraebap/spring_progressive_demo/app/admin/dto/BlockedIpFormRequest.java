package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import lombok.Data;

@Data
public class BlockedIpFormRequest {
    private String ipAddress;
    private String reason;
}
