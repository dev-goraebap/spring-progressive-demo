package xyz.goraebap.spring_progressive_demo.shared.vite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class ViteControllerAdvice {

    private final ViteManifest viteManifest;

    @ModelAttribute("vite")
    public ViteManifest vite() {
        return viteManifest;
    }
}
