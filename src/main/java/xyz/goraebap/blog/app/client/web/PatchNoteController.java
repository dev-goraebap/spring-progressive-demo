package xyz.goraebap.blog.app.client.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import xyz.goraebap.blog.app.client.dto.PatchNoteIndexRequest;
import xyz.goraebap.blog.infra.service.PostQueryService;

@Controller
@RequestMapping("/patch-notes")
@RequiredArgsConstructor
public class PatchNoteController {

    private final PostQueryService postQueryService;
    private final ViewCountHelper viewCountHelper;

    @GetMapping
    public String index(
            Model model,
            @Valid @ModelAttribute("req") PatchNoteIndexRequest req,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest,
            @RequestHeader(value = "HX-Boosted", required = false) boolean htmxBoosted
    ) {
        var postData = postQueryService.getPostsWithPagination(req.getPage(), req.getSort(), "patch-note");
        model.addAttribute("postData", postData);

        if (htmxRequest && !htmxBoosted) {
            return "pages/patchNote/index/htmxList";
        }
        return "pages/patchNote/index";
    }

    @GetMapping("/{slug}")
    public String show(
            @PathVariable String slug,
            @CookieValue(name = "viewed_posts", defaultValue = "") String viewedPosts,
            HttpServletResponse response,
            Model model
    ) {
        var post = postQueryService.getPostBySlug(slug, "patch-note");
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 조회수 증가 처리
        viewCountHelper.process(post.getId(), viewedPosts, response);

        model.addAttribute("post", post);
        return "pages/patchNote/show";
    }
}
