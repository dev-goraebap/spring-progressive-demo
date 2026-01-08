package xyz.goraebap.blog.shared.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiService {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public GeminiService(GeminiProperties properties, ObjectMapper objectMapper) {
        this.apiKey = properties.apiKey();
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(GEMINI_API_URL)
                .build();
    }

    /**
     * 이미지에서 지배적 색상 추출
     * @param imageData 이미지 바이트 배열
     * @param mimeType 이미지 MIME 타입 (image/jpeg, image/png 등)
     * @return hex 색상 코드 (예: #FF5733) 또는 null
     */
    public String extractDominantColor(byte[] imageData, String mimeType) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Gemini API key가 설정되지 않았습니다.");
            return null;
        }

        try {
            String base64Image = Base64.getEncoder().encodeToString(imageData);

            var requestBody = Map.of(
                "contents", List.of(Map.of(
                    "parts", List.of(
                        Map.of(
                            "inlineData", Map.of(
                                "mimeType", mimeType,
                                "data", base64Image
                            )
                        ),
                        Map.of(
                            "text", """
                                이 이미지를 대표하는 색상 1개를 hex 코드로만 응답해주세요.

                                조건:
                                - 흰색(#FFFFFF), 검은색(#000000), 회색 계열은 제외
                                - 너무 밝은 색(명도 90% 이상)이나 너무 어두운 색(명도 20% 이하)은 제외
                                - 배경색보다는 주요 피사체의 색상 선호
                                - 이 색상은 썸네일 카드 배경으로 사용됩니다
                                - 채도가 있는 색상을 우선 선택해주세요

                                예시: #FF5733
                                """
                        )
                    )
                ))
            );

            String response = restClient.post()
                    .uri("?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText().trim();

            // hex 코드 추출 (# 포함)
            if (text.matches(".*#[0-9A-Fa-f]{6}.*")) {
                int idx = text.indexOf('#');
                return text.substring(idx, idx + 7).toUpperCase();
            }

            log.warn("Gemini 응답에서 hex 코드를 찾을 수 없습니다: {}", text);
            return null;

        } catch (Exception e) {
            log.error("Gemini 색상 추출 실패", e);
            return null;
        }
    }

    /**
     * 영어 텍스트를 한국어로 번역
     * @param title 제목
     * @param snippet 요약/설명
     * @return 번역 결과
     */
    public TranslationResult translate(String title, String snippet) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Gemini API key가 설정되지 않았습니다.");
            return new TranslationResult(title, snippet);
        }

        try {
            String prompt = String.format("""
                다음은 개발 관련 게시물의 제목과 설명입니다.
                영어를 한국어로 자연스럽게 번역해주세요.
                기술 용어는 적절히 유지하거나 한국어로 번역해주세요.
                JSON 형식으로만 응답해주세요.

                제목: %s
                설명: %s

                응답 형식:
                {"title": "번역된 제목", "snippet": "번역된 설명"}
                """, title, snippet != null ? snippet : "");

            var requestBody = Map.of(
                "contents", List.of(Map.of(
                    "parts", List.of(Map.of("text", prompt))
                ))
            );

            String response = restClient.post()
                    .uri("?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText().trim();

            // JSON 파싱 (마크다운 코드블록 제거)
            text = text.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            JsonNode result = objectMapper.readTree(text);

            return new TranslationResult(
                    result.path("title").asText(title),
                    result.path("snippet").asText(snippet)
            );

        } catch (Exception e) {
            log.error("Gemini 번역 실패: title={}", title, e);
            return new TranslationResult(title, snippet);
        }
    }

    public record TranslationResult(String title, String snippet) {}
}
