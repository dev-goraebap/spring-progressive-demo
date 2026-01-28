package xyz.goraebap.blog.contract.waf;

/**
 * IP 차단 확인 및 자동 차단 인터페이스
 * - WafFilter(shared)에서 사용
 * - BlockedIpService(app)에서 구현
 */
public interface IpBlockChecker {

    /**
     * IP 차단 여부 확인
     * @param ipAddress 확인할 IP 주소
     * @return 차단된 IP면 true
     */
    boolean isBlocked(String ipAddress);

    /**
     * 자동 IP 차단 (악성 요청 감지 시)
     * @param ipAddress 차단할 IP 주소
     * @param reason 차단 사유
     * @param hours 차단 시간 (시간 단위)
     */
    void autoBlock(String ipAddress, String reason, int hours);
}
