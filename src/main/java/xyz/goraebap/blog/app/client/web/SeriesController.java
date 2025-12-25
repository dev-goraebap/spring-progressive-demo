package xyz.goraebap.blog.app.client.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import xyz.goraebap.blog.infra.service.SeriesQueryService;

@Controller
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesQueryService seriesQueryService;

    @GetMapping
    public String index(Model model) {
        var seriesList = seriesQueryService.getAllSeries();
        model.addAttribute("seriesList", seriesList);
        return "pages/series/index";
    }

    @GetMapping("/{slug}")
    public String show(@PathVariable String slug, Model model) {
        var series = seriesQueryService.getSeriesWithPosts(slug);
        if (series == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("series", series);
        return "pages/series/show";
    }
}
