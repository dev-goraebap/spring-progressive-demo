package xyz.goraebap.spring_progressive_demo.shared.vite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ViteManifest {

    private final ObjectMapper objectMapper;

    private String js;
    private String css;

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("static/.vite/manifest.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    JsonNode manifest = objectMapper.readTree(is);
                    JsonNode appEntry = manifest.get("src/app.js");
                    if (appEntry != null) {
                        this.js = appEntry.get("file").asText();
                        JsonNode cssArray = appEntry.get("css");
                        if (cssArray != null && cssArray.isArray() && !cssArray.isEmpty()) {
                            this.css = cssArray.get(0).asText();
                        }
                    }
                }
                log.info("Vite manifest loaded: js={}, css={}", js, css);
            } else {
                log.warn("Vite manifest not found, using default paths");
                this.js = "builds/app.js";
                this.css = "builds/app.css";
            }
        } catch (IOException e) {
            log.error("Failed to load Vite manifest", e);
            this.js = "builds/app.js";
            this.css = "builds/app.css";
        }
    }
}
