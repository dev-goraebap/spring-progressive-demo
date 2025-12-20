package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlobRepository extends JpaRepository<BlobEntity, Long> {
    Optional<BlobEntity> findByChecksum(String checksum);
    Optional<BlobEntity> findByKey(String key);
}
