package xyz.goraebap.blog.app.client.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.blog.app.client.dto.FeedIndexRequest;
import xyz.goraebap.blog.infra.service.CurationQueryService;
import xyz.goraebap.blog.infra.service.PostQueryService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FeedController {

    private final PostQueryService postQueryService;
    private final CurationQueryService curationQueryService;

    @GetMapping
    public String index(
            Model model,
            @ModelAttribute("req") FeedIndexRequest req,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest,
            @RequestHeader(value = "HX-Boosted", required = false) boolean htmxBoosted
    ) {
        var postData = postQueryService.getPostsWithPagination(req.getPage(), req.getSort());
        model.addAttribute("postData", postData);

        var curationItems = curationQueryService.getLatestItems(3);
        model.addAttribute("curationItems", curationItems);

        // 첫 페이지일 때만 최근 패치노트 조회
        if (req.getPage() == 1) {
            var latestPatchNote = postQueryService.getLatestPatchNote();
            model.addAttribute("latestPatchNote", latestPatchNote);
        }

        if (htmxRequest && !htmxBoosted) {
            return "pages/feed/index/htmxList";
        }
        return "pages/feed/index";
    }
}
