package xyz.goraebap.spring_progressive_demo.app.series.presentation.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.spring_progressive_demo.infra.service.SeriesQueryService;

@Controller
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesPageController {

    private final SeriesQueryService seriesQueryService;

    @GetMapping
    public String index(Model model) {
        var seriesList = seriesQueryService.getAllSeries();
        model.addAttribute("seriesList", seriesList);
        return "pages/series/index";
    }
}
