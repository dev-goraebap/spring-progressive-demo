package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminSeriesIndexRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminSeriesFormRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.SeriesService;
import xyz.goraebap.spring_progressive_demo.infra.service.SeriesQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller("adminSeriesController")
@RequestMapping("/admin/series")
@RequiredArgsConstructor
public class AdminSeriesController {

    private final SeriesQueryService seriesQueryService;
    private final SeriesService seriesService;

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

    @GetMapping("/add")
    public String add() {
        return "pages/admin/series/add";
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid AdminSeriesFormRequest req,
            @AuthenticationPrincipal Long userId
    ) {
        seriesService.create(req, userId);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "시리즈가 등록되었습니다.")
                        .build())
                .header("HX-Location", "/admin/series")
                .build();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var series = seriesQueryService.getSeriesById(id);
        model.addAttribute("series", series);
        return "pages/admin/series/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid AdminSeriesFormRequest req
    ) {
        seriesService.update(id, req);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "시리즈가 수정되었습니다.")
                        .build())
                .header("HX-Location", "/admin/series")
                .build();
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        var series = seriesQueryService.getSeriesById(id);
        model.addAttribute("series", series);
        return "pages/admin/series/remove";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "시리즈가 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/series")
                .build();
    }
}
