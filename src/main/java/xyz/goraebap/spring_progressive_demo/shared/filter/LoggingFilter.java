package xyz.goraebap.spring_progressive_demo.shared.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.goraebap.spring_progressive_demo.shared.logging.AppLogService;
import xyz.goraebap.spring_progressive_demo.shared.logging.ClientIpUtils;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class LoggingFilter implements Filter {

    private final AppLogService appLogService;

    // 로깅에서 제외할 정적 리소스 확장자
    private static final Set<String> STATIC_EXTENSIONS = Set.of(
            ".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg",
            ".ico", ".woff", ".woff2", ".ttf", ".eot", ".map", ".mp3"
    );

    // 로깅에서 제외할 경로 prefix
    private static final Set<String> EXCLUDED_PREFIXES = Set.of(
            "/builds/", "/css/", "/js/", "/images/", "/fonts/", "/bgm/", "/.vite/"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();

        // 정적 리소스는 로깅 제외
        if (isStaticResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        String method = httpRequest.getMethod();
        String queryString = httpRequest.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;
        String ip = ClientIpUtils.getRealClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        String referer = httpRequest.getHeader("Referer");

        log.info(">>> [HTTP Request] {} {} from {}", method, fullUrl, ip);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            int status = httpResponse.getStatus();
            int executionTime = (int) (System.currentTimeMillis() - startTime);

            log.info("<<< [HTTP Response] {} {} - Status: {} - {}ms", method, uri, status, executionTime);

            // 비동기로 DB 저장
            appLogService.saveHttpLog(method, fullUrl, status, executionTime, ip, userAgent, referer);
        }
    }

    private boolean isStaticResource(String uri) {
        // 확장자 체크
        for (String ext : STATIC_EXTENSIONS) {
            if (uri.endsWith(ext)) {
                return true;
            }
        }

        // 경로 prefix 체크
        for (String prefix : EXCLUDED_PREFIXES) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
