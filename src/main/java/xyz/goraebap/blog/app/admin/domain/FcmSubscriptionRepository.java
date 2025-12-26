package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmSubscriptionRepository extends JpaRepository<FcmSubscriptionEntity, Long> {

    Optional<FcmSubscriptionEntity> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token);

    long count();
}
