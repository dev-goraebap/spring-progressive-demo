package xyz.goraebap.spring_progressive_demo.app.post.presentation.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            @Valid @ParameterObject PatchNoteIndexRequest dto
    ) {
        var postData = postQueryService.getPostsWithPagination(dto.getPage(), dto.getSort(), "patch-note");
        model.addAttribute("postData", postData);
        return "pages/patch-note/index";
    }
}
