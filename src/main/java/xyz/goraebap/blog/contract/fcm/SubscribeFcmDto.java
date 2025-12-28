package xyz.goraebap.blog.contract.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeFcmDto {

    @NotBlank(message = "토큰은 필수입니다")
    private String token;

    private String ipAddress;
    private String browser;
    private String os;
    private String deviceType;
}
