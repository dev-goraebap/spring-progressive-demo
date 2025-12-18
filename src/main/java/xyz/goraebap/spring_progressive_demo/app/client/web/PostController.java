package xyz.goraebap.spring_progressive_demo.app.client.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;
import xyz.goraebap.spring_progressive_demo.infra.service.SeriesQueryService;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final SeriesQueryService seriesQueryService;
    private final ViewCountHelper viewCountHelper;

    @GetMapping("/{slug}")
    public String show(
            @PathVariable String slug,
            @CookieValue(name = "viewed_posts", defaultValue = "") String viewedPosts,
            HttpServletResponse response,
            Model model
    ) {
        var post = postQueryService.getPostBySlug(slug);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 조회수 증가 처리
        viewCountHelper.process(post.getId(), viewedPosts, response);

        // 시리즈 네비게이션 정보 조회
        var seriesNav = seriesQueryService.getSeriesNavByPostId(post.getId());

        model.addAttribute("post", post);
        model.addAttribute("seriesNav", seriesNav);
        return "pages/post/show";
    }
}
