package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuratedSourceRepository extends JpaRepository<CuratedSourceEntity, Long> {

    Optional<CuratedSourceEntity> findByUrl(String url);

    List<CuratedSourceEntity> findByIsActiveYn(String isActiveYn);
}
