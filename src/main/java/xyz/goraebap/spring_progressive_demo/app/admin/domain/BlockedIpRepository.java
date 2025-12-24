package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockedIpRepository extends JpaRepository<BlockedIpEntity, Long> {

    @Query(value = "SELECT * FROM blocked_ips WHERE ip_address = CAST(:ipAddress AS inet)", nativeQuery = true)
    Optional<BlockedIpEntity> findByIpAddress(@Param("ipAddress") String ipAddress);

    /**
     * 활성 상태이고 미만료인 IP 조회 (WAF용)
     */
    @Query(value = """
        SELECT * FROM blocked_ips
        WHERE ip_address = CAST(:ipAddress AS inet)
          AND is_active_yn = 'Y'
          AND (expires_at IS NULL OR expires_at > CURRENT_TIMESTAMP)
    """, nativeQuery = true)
    Optional<BlockedIpEntity> findActiveByIpAddress(String ipAddress);

    /**
     * 만료된 IP 일괄 비활성화
     */
    @Modifying
    @Query("""
        UPDATE BlockedIpEntity b
        SET b.isActiveYn = 'N', b.updatedAt = CURRENT_TIMESTAMP
        WHERE b.isActiveYn = 'Y'
          AND b.expiresAt IS NOT NULL
          AND b.expiresAt < CURRENT_TIMESTAMP
    """)
    int expireOldBlocks();
}
