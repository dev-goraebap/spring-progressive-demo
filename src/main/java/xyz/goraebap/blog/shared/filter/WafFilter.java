package xyz.goraebap.blog.shared.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.goraebap.blog.app.admin.service.BlockedIpService;
import xyz.goraebap.blog.shared.logging.ClientIpUtils;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * WAF (Web Application Firewall) 필터
 * - IP 차단 확인
 * - 악성 경로 패턴 감지 (자동 차단)
 * - 악성 User-Agent 감지 (자동 차단)
 */
@Slf4j
@Component
@Order(0) // LoggingFilter보다 먼저 실행
@RequiredArgsConstructor
public class WafFilter implements Filter {

    private final BlockedIpService blockedIpService;

    private static final int AUTO_BLOCK_HOURS = 24;

    // 악성 경로 패턴
    private static final Set<String> MALICIOUS_PATHS = Set.of(
            // PHP 관련
            ".php", "/wp-admin", "/wp-login", "/wp-config", "/wp-includes", "/wordpress",
            "/systembc", "/xmlrpc.php",
            // 환경 설정 파일
            "/.env", "/.git", "/.svn", "/.htaccess",
            "/config.php", "/database.php", "/settings.php",
            // 일반적인 취약점 스캔
            "/backup", "/phpmyadmin", "/adminer", "/sql", "/mysql",
            "/readme.txt", "/license.txt", "/changelog.txt",
            // 디렉토리 스캔
            "/cgi-bin", "/scripts", "/shell", "/cmd",
            // API 스캔
            "/api/v1/users", "/rest/api", "/graphql"
    );

    // 악성 User-Agent 패턴
    private static final Pattern MALICIOUS_USER_AGENT_PATTERN = Pattern.compile(
            ".*(sqlmap|nikto|nmap|masscan|zgrab|shodan|censys|scanner|crawler|spider|bot).*",
            Pattern.CASE_INSENSITIVE
    );

    // WAF 검사에서 제외할 경로 prefix
    private static final Set<String> EXCLUDED_PREFIXES = Set.of(
            "/builds/", "/css/", "/js/", "/images/", "/fonts/", "/bgm/",
            "/admin/" // 관리자 페이지는 제외 (로그인 필요)
    );

    // 정적 리소스 확장자 (WAF 검사 제외)
    private static final Set<String> STATIC_EXTENSIONS = Set.of(
            ".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg",
            ".ico", ".woff", ".woff2", ".ttf", ".eot", ".map", ".mp3"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI().toLowerCase();
        String ip = ClientIpUtils.getRealClientIp(httpRequest);

        // 정적 리소스는 WAF 검사 제외
        if (isExcluded(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // 1. IP 차단 확인
        if (blockedIpService.isBlocked(ip)) {
            log.warn("[WAF] Blocked IP access attempt: {} -> {}", ip, uri);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        // 2. 악성 경로 패턴 감지
        String maliciousPath = detectMaliciousPath(uri);
        if (maliciousPath != null) {
            String reason = "악성 경로 접근: " + maliciousPath;
            log.warn("[WAF] Malicious path detected: {} from {} - auto blocking", uri, ip);
            blockedIpService.autoBlock(ip, reason, AUTO_BLOCK_HOURS);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        // 3. 악성 User-Agent 감지
        String userAgent = httpRequest.getHeader("User-Agent");
        if (isMaliciousUserAgent(userAgent)) {
            String reason = "악성 User-Agent: " + (userAgent != null ? userAgent.substring(0, Math.min(100, userAgent.length())) : "empty");
            log.warn("[WAF] Malicious User-Agent detected: {} from {} - auto blocking", userAgent, ip);
            blockedIpService.autoBlock(ip, reason, AUTO_BLOCK_HOURS);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isExcluded(String uri) {
        // 정적 리소스 확장자 확인
        for (String ext : STATIC_EXTENSIONS) {
            if (uri.endsWith(ext)) {
                return true;
            }
        }

        // 제외 경로 확인
        for (String prefix : EXCLUDED_PREFIXES) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    private String detectMaliciousPath(String uri) {
        for (String pattern : MALICIOUS_PATHS) {
            if (uri.contains(pattern)) {
                return pattern;
            }
        }
        return null;
    }

    private boolean isMaliciousUserAgent(String userAgent) {
        // 빈 User-Agent는 의심
        if (userAgent == null || userAgent.isEmpty()) {
            return true;
        }

        return MALICIOUS_USER_AGENT_PATTERN.matcher(userAgent).matches();
    }
}
