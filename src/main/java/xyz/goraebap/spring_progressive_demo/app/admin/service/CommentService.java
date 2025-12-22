package xyz.goraebap.spring_progressive_demo.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.CommentEntity;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.CommentRepository;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.PostRepository;
import xyz.goraebap.spring_progressive_demo.contract.comment.CreateCommentDto;
import xyz.goraebap.spring_progressive_demo.contract.comment.CommentCreator;
import xyz.goraebap.spring_progressive_demo.shared.exception.BadRequestException;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentCreator {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void create(String requestId, String postSlug, CreateCommentDto dto) {
        // 중복 요청 체크
        if (commentRepository.existsByRequestId(requestId)) {
            throw new BadRequestException("이미 처리된 요청입니다.");
        }

        // 게시글 존재 확인
        var post = postRepository.findBySlug(postSlug)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        CommentEntity commentEntity = CommentEntity.create(
                requestId,
                post.getId(),
                dto.nickname(),
                dto.comment(),
                dto.avatarNo()
        );

        commentRepository.save(commentEntity);
    }
}
