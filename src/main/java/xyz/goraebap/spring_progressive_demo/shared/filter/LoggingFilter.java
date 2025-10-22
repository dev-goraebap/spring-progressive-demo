package xyz.goraebap.spring_progressive_demo.shared.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청 정보
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String ip = httpRequest.getRemoteAddr();

        log.info(">>> [HTTP Request] {} {} {} from {}",
                method,
                uri,
                queryString != null ? "?" + queryString : "",
                ip);

        long startTime = System.currentTimeMillis();

        try {
            // 다음 필터/서블릿으로 전달
            chain.doFilter(request, response);
        } finally {
            // 응답 정보
            int status = httpResponse.getStatus();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("<<< [HTTP Response] {} {} - Status: {} - {}ms",
                    method,
                    uri,
                    status,
                    executionTime);
        }
    }
}
