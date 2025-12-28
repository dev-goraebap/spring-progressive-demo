package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FcmSubscriptionRepository extends JpaRepository<FcmSubscriptionEntity, Long> {

    Optional<FcmSubscriptionEntity> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token);

    long count();

    @Query("SELECT f.token FROM FcmSubscriptionEntity f")
    List<String> findAllTokens();
}
