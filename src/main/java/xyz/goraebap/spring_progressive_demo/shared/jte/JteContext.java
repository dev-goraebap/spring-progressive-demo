package xyz.goraebap.spring_progressive_demo.shared.jte;

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
        if (attributes != null) {
            return attributes.getRequest().getRequestURI();
        }
        return "";
    }

    public static boolean isCurrentPath(String path) {
        return currentPath().equals(path);
    }

    public static String getQueryParam(String name) {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String value = attributes.getRequest().getParameter(name);
            return value != null ? value : "";
        }
        return "";
    }

    public static boolean isQueryParam(String name, String value) {
        return getQueryParam(name).equals(value);
    }
}
