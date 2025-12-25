package xyz.goraebap.blog.infra.view_model;

public interface ThumbnailEnrichable {
    String getThumbnailKey();
    String getThumbnailMetadata();
    void setThumbnailUrl(String url);
    void setThumbnailDominantColor(String color);
}
