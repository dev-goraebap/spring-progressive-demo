package xyz.goraebap.spring_progressive_demo.infra.view_model;

public interface ThumbnailEnrichable {
    String getThumbnailKey();
    String getThumbnailMetadata();
    void setThumbnailUrl(String url);
    void setThumbnailDominantColor(String color);
}
