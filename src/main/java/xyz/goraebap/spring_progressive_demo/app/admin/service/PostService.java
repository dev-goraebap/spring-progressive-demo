package xyz.goraebap.spring_progressive_demo.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.AttachmentEntity;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.AttachmentRepository;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.PostEntity;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.PostRepository;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostCreateRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminPostUpdateRequest;
import xyz.goraebap.spring_progressive_demo.contract.post.ViewCounter;
import xyz.goraebap.spring_progressive_demo.shared.exception.BadRequestException;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements ViewCounter {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

    public PostEntity create(AdminPostCreateRequest req, Long userId) {
        // slug 중복 검증
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            if (postRepository.findBySlug(req.getSlug()).isPresent()) {
                throw new BadRequestException("이미 사용 중인 슬러그입니다.");
            }
        }

        var post = PostEntity.create(req, userId);
        postRepository.save(post);

        // 썸네일 첨부
        if (req.getThumbnailBlobId() != null) {
            attachThumbnail(post.getId(), req.getThumbnailBlobId());
        }

        return post;
    }

    public PostEntity update(Long postId, AdminPostUpdateRequest req) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));

        // slug 변경 시 중복 검증
        if (req.getSlug() != null && !req.getSlug().isBlank() && !req.getSlug().equals(post.getSlug())) {
            if (postRepository.findBySlug(req.getSlug()).isPresent()) {
                throw new BadRequestException("이미 사용 중인 슬러그입니다.");
            }
        }

        post.update(req);
        postRepository.save(post);

        // 썸네일 업데이트
        if (req.getThumbnailBlobId() != null) {
            attachThumbnail(postId, req.getThumbnailBlobId());
        }

        return post;
    }

    private void attachThumbnail(Long postId, Long blobId) {
        String recordType = "post";
        String recordId = postId.toString();
        String name = "thumbnail";

        // 기존 썸네일 삭제
        attachmentRepository.deleteByRecordTypeAndRecordIdAndName(recordType, recordId, name);

        // 새 썸네일 첨부
        var attachment = AttachmentEntity.create(name, recordType, recordId, blobId);
        attachmentRepository.save(attachment);
    }

    public void delete(Long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    @Async
    public void incrementViewCount(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.incrementViewCount();
            postRepository.save(post);
        });
    }
}
