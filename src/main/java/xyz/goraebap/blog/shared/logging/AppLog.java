package xyz.goraebap.blog.shared.logging;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_logs", indexes = {
        @Index(name = "idx_app_logs_timestamp", columnList = "timestamp DESC"),
        @Index(name = "idx_app_logs_level", columnList = "level"),
        @Index(name = "idx_app_logs_ip_address", columnList = "ipAddress")
})
@Getter
@NoArgsConstructor
public class AppLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 10)
    private String level;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(length = 10)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String url;

    private Integer statusCode;

    private Integer responseTime;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 500)
    private String referer;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(columnDefinition = "TEXT")
    private String errorStack;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static AppLog createHttpLog(
            String method,
            String url,
            int statusCode,
            int responseTime,
            String ipAddress,
            String userAgent,
            String referer
    ) {
        AppLog log = new AppLog();
        log.timestamp = LocalDateTime.now();
        log.level = statusCode >= 400 ? "ERROR" : "INFO";
        log.message = String.format("%s %s %d %dms", method, url, statusCode, responseTime);
        log.method = method;
        log.url = url;
        log.statusCode = statusCode;
        log.responseTime = responseTime;
        log.ipAddress = ipAddress;
        log.userAgent = userAgent;
        log.referer = referer;
        return log;
    }

    public void setError(String errorMessage, String errorStack) {
        this.errorMessage = errorMessage;
        this.errorStack = errorStack;
        this.level = "ERROR";
    }
}
