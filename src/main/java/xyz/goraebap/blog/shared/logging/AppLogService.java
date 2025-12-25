package xyz.goraebap.blog.shared.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppLogService {

    private final AppLogRepository appLogRepository;

    @Async
    public void saveHttpLog(String method, String url, int statusCode, int responseTime,
                            String ipAddress, String userAgent, String referer) {
        try {
            AppLog appLog = AppLog.createHttpLog(
                    method, url, statusCode, responseTime, ipAddress, userAgent, referer
            );
            appLogRepository.save(appLog);
        } catch (Exception e) {
            log.error("Failed to save log to database: {}", e.getMessage());
        }
    }
}
