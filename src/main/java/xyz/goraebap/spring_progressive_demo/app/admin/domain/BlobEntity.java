package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String filename;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "byte_size", nullable = false)
    private Integer byteSize;

    @Column(nullable = false, unique = true)
    private String checksum;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static BlobEntity create(String key, String filename, String contentType,
                                     int byteSize, String checksum, String createdBy, String metadata) {
        var entity = new BlobEntity();
        entity.key = key;
        entity.filename = filename;
        entity.contentType = contentType;
        entity.serviceName = "r2";
        entity.byteSize = byteSize;
        entity.checksum = checksum;
        entity.createdBy = createdBy;
        entity.metadata = metadata != null ? metadata : "{}";
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}
