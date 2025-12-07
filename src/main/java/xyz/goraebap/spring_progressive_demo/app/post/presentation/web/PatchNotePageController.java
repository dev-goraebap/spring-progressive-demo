package xyz.goraebap.spring_progressive_demo.app.post.presentation.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import xyz.goraebap.spring_progressive_demo.app.post.dto.PatchNoteIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;

@Controller
@RequestMapping("/patch-notes")
@RequiredArgsConstructor
public class PatchNotePageController {

    private final PostQueryService postQueryService;

    @GetMapping
    public String index(
            Model model,
            @Valid @ParameterObject PatchNoteIndexRequest dto,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest,
            @RequestHeader(value = "HX-Boosted", required = false) boolean htmxBoosted
    ) {
        var postData = postQueryService.getPostsWithPagination(dto.getPage(), dto.getSort(), "patch-note");
        model.addAttribute("postData", postData);

        if (htmxRequest && !htmxBoosted) {
            return "pages/patch-note/_list";
        }
        return "pages/patch-note/index";
    }

    @GetMapping("/{slug}")
    public String show(
            @PathVariable String slug,
            Model model
    ) {
        var post = postQueryService.getPostBySlug(slug, "patch-note");
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("post", post);
        return "pages/patch-note/show";
    }
}
