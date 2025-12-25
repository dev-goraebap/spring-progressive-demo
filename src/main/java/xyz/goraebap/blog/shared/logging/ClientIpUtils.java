package xyz.goraebap.blog.shared.logging;

import jakarta.servlet.http.HttpServletRequest;

public class ClientIpUtils {

    private ClientIpUtils() {}

    /**
     * 프록시 환경에서 실제 클라이언트 IP를 추출합니다.
     * 우선순위: X-Forwarded-For -> X-Real-IP -> CF-Connecting-IP -> remoteAddr
     */
    public static String getRealClientIp(HttpServletRequest request) {
        // X-Forwarded-For (가장 일반적)
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            // 첫 번째 IP가 실제 클라이언트 IP
            return forwardedFor.split(",")[0].trim();
        }

        // X-Real-IP (Nginx)
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp;
        }

        // CF-Connecting-IP (Cloudflare)
        String cfConnectingIp = request.getHeader("CF-Connecting-IP");
        if (cfConnectingIp != null && !cfConnectingIp.isEmpty()) {
            return cfConnectingIp;
        }

        // 기본값
        return request.getRemoteAddr();
    }
}
