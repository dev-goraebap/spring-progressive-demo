package xyz.goraebap.spring_progressive_demo.app.feed.presentation.web;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.feed.dto.WebFeedIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.mapper.PostViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModelEnricher;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FeedPageController {

    private final PostViewMapper postViewMapper;
    private final PostViewModelEnricher postViewModelEnricher;

    @GetMapping
    public String index(
            Model model,
            @ParameterObject WebFeedIndexRequest dto,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest
    ) {
        List<PostViewModel> posts = postViewModelEnricher.withThumbnails(
                postViewMapper.findPosts(dto.getOrderType(), dto.getLimit(), dto.getOffset())
        );
        int totalCount = postViewMapper.countPosts();
        boolean hasMore = (dto.getOffset() + posts.size()) < totalCount;

        model.addAttribute("posts", posts);
        model.addAttribute("hasMore", hasMore);
        model.addAttribute("nextPage", dto.getNextPage());
        model.addAttribute("orderType", dto.getOrderType());

        if (htmxRequest && dto.getPage() > 0) {
            return "fragments/postListItems";
        }
        return "pages/feed/index";
    }
}
