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
@Component
@RequiredArgsConstructor
public class ViteManifest {

    private final ObjectMapper objectMapper;
    private final java.util.Map<String, EntryAssets> entries = new java.util.HashMap<>();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("static/.vite/manifest.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    JsonNode manifest = objectMapper.readTree(is);
                    manifest.fields().forEachRemaining(field -> {
                        String path = field.getKey();
                        JsonNode entry = field.getValue();
                        // JS 엔트리포인트 파싱
                        if (path.startsWith("src/app/") && path.endsWith(".js")) {
                            String name = path.replace("src/app/", "").replace(".js", "");
                            String js = entry.get("file").asText();
                            entries.put(name, new EntryAssets(js));
                        }
                        // CSS 엔트리포인트 파싱
                        if (path.equals("src/app/style.css")) {
                            cssFile = entry.get("file").asText();
                        }
                    });
                }
                log.info("Vite manifest loaded: {}", entries.keySet());
            } else {
                log.warn("Vite manifest not found");
            }
        } catch (IOException e) {
            log.error("Failed to load Vite manifest", e);
        }
    }

    public String getJs(String entryName) {
        EntryAssets assets = entries.get(entryName);
        return assets != null ? assets.js() : "builds/" + entryName + ".js";
    }

    private String cssFile;

    public String getCss() {
        return cssFile != null ? cssFile : "builds/style.css";
    }

    private record EntryAssets(String js) {}
}
