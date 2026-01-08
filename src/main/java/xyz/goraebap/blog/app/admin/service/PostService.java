package xyz.goraebap.blog.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.AttachmentEntity;
import xyz.goraebap.blog.app.admin.domain.AttachmentRepository;
import xyz.goraebap.blog.app.admin.domain.PostEntity;
import xyz.goraebap.blog.app.admin.domain.PostRepository;
import xyz.goraebap.blog.app.admin.dto.AdminPostFormRequest;
import xyz.goraebap.blog.contract.post.ViewCounter;
import xyz.goraebap.blog.shared.exception.BadRequestException;
import xyz.goraebap.blog.shared.exception.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements ViewCounter {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

    public PostEntity create(AdminPostFormRequest req, Long userId) {
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

    public void update(Long postId, AdminPostFormRequest req) {
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

        // 썸네일 처리
        if (Boolean.TRUE.equals(req.getClearThumbnail())) {
            detachThumbnail(postId);
        } else if (req.getThumbnailBlobId() != null) {
            attachThumbnail(postId, req.getThumbnailBlobId());
        }
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

    private void detachThumbnail(Long postId) {
        String recordType = "post";
        String recordId = postId.toString();
        String name = "thumbnail";
        attachmentRepository.deleteByRecordTypeAndRecordIdAndName(recordType, recordId, name);
    }
}
