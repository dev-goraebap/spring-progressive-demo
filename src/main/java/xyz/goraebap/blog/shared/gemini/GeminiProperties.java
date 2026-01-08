package xyz.goraebap.blog.shared.gemini;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.gemini")
public record GeminiProperties(String apiKey) {}
