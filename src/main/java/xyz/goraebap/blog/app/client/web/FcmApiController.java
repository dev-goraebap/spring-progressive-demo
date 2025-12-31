package xyz.goraebap.blog.app.client.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam String token) {
        return ResponseEntity.ok(fcmSubscriber.isSubscribed(token));
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
