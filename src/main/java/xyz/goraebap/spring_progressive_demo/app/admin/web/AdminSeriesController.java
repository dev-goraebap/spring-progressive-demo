package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminSeriesIndexRequest;
import xyz.goraebap.spring_progressive_demo.infra.service.SeriesQueryService;

@Controller
@RequestMapping("/admin/series")
@RequiredArgsConstructor
public class AdminSeriesController {

    private final SeriesQueryService seriesQueryService;

    @GetMapping
    public String index(@ModelAttribute("req") AdminSeriesIndexRequest req, Model model) {
        var seriesData = seriesQueryService.getAdminSeriesWithPagination(
                req.getName(),
                req.getStatus(),
                req.getIsPublishedYn(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("seriesData", seriesData);
        return "pages/admin/series/index";
    }
}
