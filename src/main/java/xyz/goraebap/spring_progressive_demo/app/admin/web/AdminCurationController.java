package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminCuratedSourceFormRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.CuratedSourceService;
import xyz.goraebap.spring_progressive_demo.infra.service.CurationQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller
@RequestMapping("/admin/curations")
@RequiredArgsConstructor
public class AdminCurationController {

    private final CurationQueryService curationQueryService;
    private final CuratedSourceService curatedSourceService;

    // 기본 경로 리다이렉트
    @GetMapping
    public String index() {
        return "redirect:/admin/curations/sources";
    }

    // ===== 소스 관리 =====

    @GetMapping("/sources")
    public String sourcesIndex(Model model) {
        var sources = curationQueryService.getAllSources();
        var stats = curationQueryService.getStats();
        model.addAttribute("sources", sources);
        model.addAttribute("stats", stats);
        return "pages/admin/curations/sources/index";
    }

    @GetMapping("/sources/add")
    public String sourcesAdd() {
        return "pages/admin/curations/sources/add";
    }

    @PostMapping("/sources")
    public ResponseEntity<Void> sourcesCreate(@Valid AdminCuratedSourceFormRequest req) {
        curatedSourceService.create(req);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "소스가 등록되었습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    @GetMapping("/sources/{id}/edit")
    public String sourcesEdit(@PathVariable Long id, Model model) {
        var source = curationQueryService.getSourceById(id);
        model.addAttribute("source", source);
        return "pages/admin/curations/sources/edit";
    }

    @PutMapping("/sources/{id}")
    public ResponseEntity<Void> sourcesUpdate(
            @PathVariable Long id,
            @Valid AdminCuratedSourceFormRequest req
    ) {
        curatedSourceService.update(id, req);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "소스가 수정되었습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    @PutMapping("/sources/{id}/toggle")
    public ResponseEntity<Void> sourcesToggle(@PathVariable Long id) {
        curatedSourceService.toggleActive(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "활성화 상태가 변경되었습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    @GetMapping("/sources/{id}/remove")
    public String sourcesRemove(@PathVariable Long id, Model model) {
        var source = curationQueryService.getSourceById(id);
        model.addAttribute("source", source);
        return "pages/admin/curations/sources/remove";
    }

    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> sourcesDestroy(@PathVariable Long id) {
        curatedSourceService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "소스가 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    @PostMapping("/sources/{id}/fetch")
    public ResponseEntity<Void> sourcesFetch(@PathVariable Long id) {
        int count = curatedSourceService.fetchFromSource(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", count + "개의 새 항목을 가져왔습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    @PostMapping("/sources/fetch-all")
    public ResponseEntity<Void> sourcesFetchAll() {
        var result = curatedSourceService.fetchAllActiveSources();
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "총 " + result.total() + "개의 새 항목을 가져왔습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/sources")
                .build();
    }

    // ===== 항목 관리 =====

    @GetMapping("/items")
    public String itemsIndex(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) Long sourceId,
            Model model
    ) {
        var itemsData = curationQueryService.getAdminItemsWithPagination(page, sort, sourceId);
        var sources = curationQueryService.getAllSources();
        model.addAttribute("itemsData", itemsData);
        model.addAttribute("sources", sources);
        model.addAttribute("sourceId", sourceId);
        return "pages/admin/curations/items/index";
    }

    @GetMapping("/items/{id}/remove")
    public String itemsRemove(@PathVariable Long id, Model model) {
        model.addAttribute("itemId", id);
        return "pages/admin/curations/items/remove";
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> itemsDestroy(@PathVariable Long id) {
        curatedSourceService.deleteItem(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "항목이 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/curations/items")
                .build();
    }
}
