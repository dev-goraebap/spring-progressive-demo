package xyz.goraebap.spring_progressive_demo.shared.logging;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppLogRepository extends JpaRepository<AppLog, Long> {
}
