package xyz.goraebap.spring_progressive_demo.app.series.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/series")
public class SeriesPageController {

    @GetMapping
    public String index(Model model) {
        return "pages/series/index";
    }
}
