package xyz.goraebap.spring_progressive_demo.app.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "record_type", nullable = false)
    private String recordType;

    @Column(name = "record_id", nullable = false)
    private String recordId;

    @Column(name = "blob_id", nullable = false)
    private Long blobId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static AttachmentEntity create(String name, String recordType, String recordId, Long blobId) {
        var entity = new AttachmentEntity();
        entity.name = name;
        entity.recordType = recordType;
        entity.recordId = recordId;
        entity.blobId = blobId;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}
