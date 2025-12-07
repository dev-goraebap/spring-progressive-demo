package xyz.goraebap.spring_progressive_demo.app.feed;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.feed.dto.FeedIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.CurationQueryService;
import xyz.goraebap.spring_progressive_demo.infra.service.PostQueryService;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FeedController {

    private final PostQueryService postQueryService;
    private final CurationQueryService curationQueryService;

    @GetMapping
    public String index(
            Model model,
            @ParameterObject FeedIndexRequest dto,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest,
            @RequestHeader(value = "HX-Boosted", required = false) boolean htmxBoosted
    ) {
        Pagination<PostViewModel> postData = postQueryService.getPostsWithPagination(dto.getPage(), dto.getSort());
        model.addAttribute("postData", postData);

        var curationItems = curationQueryService.getLatestItems(3);
        model.addAttribute("curationItems", curationItems);

        // 첫 페이지일 때만 최근 패치노트 조회
        if (dto.getPage() == 1) {
            var latestPatchNote = postQueryService.getLatestPatchNote();
            model.addAttribute("latestPatchNote", latestPatchNote);
        }

        if (htmxRequest && !htmxBoosted) {
            return "pages/feed/_list";
        }
        return "pages/feed/index";
    }
}
