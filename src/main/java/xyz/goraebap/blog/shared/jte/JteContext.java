package xyz.goraebap.blog.shared.jte;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.goraebap.blog.shared.vite.ViteManifest;

@Component
@Slf4j
public class JteContext {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static ViteManifest viteManifest;
    private static String activeProfile;

    public JteContext(ViteManifest viteManifest, org.springframework.core.env.Environment environment) {
        JteContext.viteManifest = viteManifest;
        JteContext.activeProfile = environment.getActiveProfiles().length > 0
            ? environment.getActiveProfiles()[0]
            : "default";
    }

    public static boolean isDev() {
        return "local".equals(activeProfile);
    }

    public static String viteCss() {
        return viteManifest != null ? viteManifest.getCss() : "builds/style.css";
    }

    public static String viteJs(String entry) {
        return viteManifest != null ? viteManifest.getJs(entry) : "builds/" + entry + ".js";
    }

    public static String currentPath() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "";
        }
        return attributes.getRequest().getRequestURI();
    }

    public static boolean isCurrentPath(String path) {
        return currentPath().equals(path);
    }

    public static boolean isPathStartsWith(String prefix) {
        return currentPath().startsWith(prefix);
    }

    public static String currentUrl() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "";
        }
        var request = attributes.getRequest();
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (scheme == null) {
            scheme = request.getScheme();
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getServerName();
            int port = request.getServerPort();
            if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
                host += ":" + port;
            }
        }
        return scheme + "://" + host + request.getRequestURI();
    }

    private static final java.util.Set<String> VALID_THEMES = java.util.Set.of("dark", "light");
    private static final String DEFAULT_THEME = "dark";

    public static String getTheme() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return DEFAULT_THEME;
        }

        Cookie[] cookies = attributes.getRequest().getCookies();
        if (cookies == null) {
            return DEFAULT_THEME;
        }

        for (Cookie cookie : cookies) {
            if ("theme".equals(cookie.getName())) {
                String theme = cookie.getValue();
                // 유효한 테마만 허용, 그 외는 기본값 반환
                return VALID_THEMES.contains(theme) ? theme : DEFAULT_THEME;
            }
        }
        return DEFAULT_THEME;
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 실패", e);
            return "{}";
        }
    }
}
