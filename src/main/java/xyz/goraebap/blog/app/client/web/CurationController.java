package xyz.goraebap.blog.app.client.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.blog.app.client.dto.CurationIndexRequest;
import xyz.goraebap.blog.infra.service.CurationQueryService;

@Controller
@RequestMapping("/curations")
@RequiredArgsConstructor
public class CurationController {

    private final CurationQueryService curationQueryService;

    @GetMapping
    public String index(
            Model model,
            @ModelAttribute("req") CurationIndexRequest req,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest,
            @RequestHeader(value = "HX-Boosted", required = false) boolean htmxBoosted
    ) {
        var curationData = curationQueryService.getItemsWithPagination(req.getPage(), req.getSort());
        model.addAttribute("curationData", curationData);

        if (htmxRequest && !htmxBoosted) {
            return "pages/curation/index/htmxList";
        }
        return "pages/curation/index";
    }

    @GetMapping("/latest")
    public String getLatestList(Model model) {
        var items = curationQueryService.getLatestItems(3);
        model.addAttribute("items", items);
        return "pages/feed/index/curationNews";
    }
}
