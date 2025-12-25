package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockedIpRepository extends JpaRepository<BlockedIpEntity, Long> {

    Optional<BlockedIpEntity> findByIpAddress(String ipAddress);

    /**
     * 활성 상태이고 미만료인 IP 조회 (WAF용)
     */
    @Query("""
        SELECT b FROM BlockedIpEntity b
        WHERE b.ipAddress = :ipAddress
          AND b.isActiveYn = 'Y'
          AND (b.expiresAt IS NULL OR b.expiresAt > CURRENT_TIMESTAMP)
    """)
    Optional<BlockedIpEntity> findActiveByIpAddress(@Param("ipAddress") String ipAddress);

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
