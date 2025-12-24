package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BlockedIpFormRequest {

    @NotBlank(message = "IP 주소를 입력해주세요.")
    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}|([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|::1|::)$",
            message = "올바른 IP 주소 형식이 아닙니다. (예: 192.168.1.1)"
    )
    private String ipAddress;

    private String reason;
}
