package xyz.goraebap.spring_progressive_demo.app.series.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/series")
@RequiredArgsConstructor
public class AdminSeriesController {

    @GetMapping
    public String index() {
        return "pages/admin/series/index";
    }
}
