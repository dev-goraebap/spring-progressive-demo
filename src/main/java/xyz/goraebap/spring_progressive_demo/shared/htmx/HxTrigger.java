package xyz.goraebap.spring_progressive_demo.shared.htmx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class HxTrigger {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Object> triggers = new LinkedHashMap<>();

    private HxTrigger() {}

    public static HxTrigger builder() {
        return new HxTrigger();
    }

    public HxTrigger toast(String type, String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8)
                .replace("+", "%20");
        triggers.put("toast", Map.of("type", type, "message", encodedMessage));
        return this;
    }

    public HxTrigger closeModal() {
        triggers.put("closeModal", true);
        return this;
    }

    public String build() {
        try {
            return objectMapper.writeValueAsString(triggers);
        } catch (JsonProcessingException e) {
            log.error("HxTrigger JSON 직렬화 실패", e);
            return "{}";
        }
    }
}
