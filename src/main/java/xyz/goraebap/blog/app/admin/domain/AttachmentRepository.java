package xyz.goraebap.blog.app.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    Optional<AttachmentEntity> findByRecordTypeAndRecordIdAndName(String recordType, String recordId, String name);
    void deleteByRecordTypeAndRecordIdAndName(String recordType, String recordId, String name);
    void deleteByRecordTypeAndRecordId(String recordType, String recordId);
}
