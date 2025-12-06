package xyz.goraebap.spring_progressive_demo.shared.jte;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.goraebap.spring_progressive_demo.shared.vite.ViteManifest;

@Component
@Slf4j
public class JteContext {

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
        return viteManifest != null ? viteManifest.getCss() : "builds/app.css";
    }

    public static String viteJs() {
        return viteManifest != null ? viteManifest.getJs() : "builds/app.js";
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

    public static String getQueryParam(String name) {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "";
        }
        String value = attributes.getRequest().getParameter(name);
        return value != null ? value : "";
    }

    public static boolean isQueryParam(String name, String value) {
        return getQueryParam(name).equals(value);
    }

    public static String getTheme() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        Cookie[] cookies = attributes.getRequest().getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("theme".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
