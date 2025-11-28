package xyz.goraebap.spring_progressive_demo.shared.vite;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class ViteControllerAdvice {

    private final ViteManifest viteManifest;
    private final Environment environment;

    @ModelAttribute("isDev")
    public boolean isDev() {
        return environment.acceptsProfiles(org.springframework.core.env.Profiles.of("local"));
    }

    @ModelAttribute("viteCss")
    public String viteCss() {
        return viteManifest.getCss();
    }

    @ModelAttribute("viteJs")
    public String viteJs() {
        return viteManifest.getJs();
    }
}
