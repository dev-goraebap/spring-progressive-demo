package xyz.goraebap.blog.app.client.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.goraebap.blog.contract.fcm.FcmSubscriber;
import xyz.goraebap.blog.contract.fcm.SubscribeFcmDto;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmApiController {

    private final FcmSubscriber fcmSubscriber;

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(
            @Valid @RequestBody SubscribeFcmDto dto,
            HttpServletRequest httpRequest
    ) {
        dto.setIpAddress(getClientIp(httpRequest));
        fcmSubscriber.subscribe(dto);
        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
