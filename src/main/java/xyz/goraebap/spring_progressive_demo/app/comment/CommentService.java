package xyz.goraebap.spring_progressive_demo.app.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.comment.domain.CommentEntity;
import xyz.goraebap.spring_progressive_demo.app.comment.domain.CommentRepository;
import xyz.goraebap.spring_progressive_demo.app.comment.dto.CreateCommentRequest;
import xyz.goraebap.spring_progressive_demo.app.post.domain.PostRepository;
import xyz.goraebap.spring_progressive_demo.infra.mapper.CommentViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.CommentViewModel;
import xyz.goraebap.spring_progressive_demo.shared.exception.BadRequestException;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentViewMapper commentViewMapper;
    private final PostRepository postRepository;

    public List<CommentViewModel> getComments(String postSlug) {
        return commentViewMapper.findByPostSlug(postSlug);
    }

    public int getCommentCount(String postSlug) {
        return commentViewMapper.countByPostSlug(postSlug);
    }

    @Transactional
    public void create(String requestId, String postSlug, CreateCommentRequest request) {
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
                request.nickname(),
                request.comment(),
                request.avatarNo()
        );

        commentRepository.save(commentEntity);
    }

    public String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
