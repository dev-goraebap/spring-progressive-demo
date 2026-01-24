package xyz.goraebap.blog.app.client.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.blog.contract.comment.CreateCommentDto;
import xyz.goraebap.blog.contract.comment.CommentCreator;
import xyz.goraebap.blog.infra.service.CommentQueryService;

import java.util.UUID;

/**
 * 댓글 컨트롤러 (클라이언트)
 * - 게시물별 댓글 목록 조회
 * - 댓글 작성
 */
@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCreator commentCreator;
    private final CommentQueryService commentQueryService;

    /**
     * 댓글 목록 조회
     * @param postSlug 게시물 슬러그
     */
    @GetMapping("/{postSlug}")
    public String index(
            @PathVariable String postSlug,
            Model model
    ) {
        var comments = commentQueryService.getCommentsByPostSlug(postSlug);

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("requestId", UUID.randomUUID().toString());

        return "entities/comment/index";
    }

    /**
     * 댓글 작성
     * @param postSlug 게시물 슬러그
     * @param requestId 중복 요청 방지용 UUID
     * @param request 댓글 작성 요청
     */
    @PostMapping("/{postSlug}")
    public String create(
            @PathVariable String postSlug,
            @RequestParam String requestId,
            @Valid CreateCommentDto request,
            Model model
    ) {
        commentCreator.create(requestId, postSlug, request);

        var comments = commentQueryService.getCommentsByPostSlug(postSlug);

        model.addAttribute("postSlug", postSlug);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("requestId", UUID.randomUUID().toString());

        return "entities/comment/index";
    }
}
