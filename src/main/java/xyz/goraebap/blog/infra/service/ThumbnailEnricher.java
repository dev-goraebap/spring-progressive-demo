package xyz.goraebap.blog.infra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.goraebap.blog.infra.view_model.ThumbnailEnrichable;
import xyz.goraebap.blog.shared.config.R2Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThumbnailEnricher {

    private final R2Properties r2Properties;
    private final ObjectMapper objectMapper;

    public void enrich(ThumbnailEnrichable item) {
        if (item.getThumbnailKey() != null) {
            item.setThumbnailUrl(r2Properties.getPublicUrl(item.getThumbnailKey()));
        }

        if (item.getThumbnailMetadata() != null) {
            try {
                JsonNode metadata = objectMapper.readTree(item.getThumbnailMetadata());
                if (metadata.has("dominantColor")) {
                    item.setThumbnailDominantColor(metadata.get("dominantColor").asText());
                }
            } catch (Exception e) {
                log.warn("Failed to parse thumbnail metadata: {}", e.getMessage());
            }
        }
    }
}
