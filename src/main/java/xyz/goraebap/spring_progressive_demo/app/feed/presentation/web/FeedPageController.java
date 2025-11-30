package xyz.goraebap.spring_progressive_demo.app.feed.presentation.web;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.feed.dto.WebFeedIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FeedPageController {

    private final PostQueryService postQueryService;

    @GetMapping
    public String index(
            Model model,
            @ParameterObject WebFeedIndexRequest dto,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest
    ) {
        Pagination<PostViewModel> postData = postQueryService.getPostsWithPagination(dto.getPage(), dto.getOrderType());
        model.addAttribute("postData", postData);
        return "pages/feed/index";
    }
}
