package xyz.goraebap.spring_progressive_demo.app.admin.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminTagIndexRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.TagService;
import xyz.goraebap.spring_progressive_demo.infra.service.TagQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagQueryService tagQueryService;
    private final TagService tagService;

    @GetMapping
    public String index(@ModelAttribute("req") AdminTagIndexRequest req, Model model) {
        var tagData = tagQueryService.getAdminTagsWithPagination(
                req.getName(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("tagData", tagData);
        return "pages/admin/tags/index";
    }

    @GetMapping("/add")
    public String add() {
        return "pages/admin/tags/add";
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid TagFormRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        tagService.create(request.name(), userId);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "태그가 생성되었습니다.")
                        .build())
                .header("HX-Location", "/admin/tags")
                .build();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var tag = tagQueryService.getTagById(id);
        model.addAttribute("tag", tag);
        return "pages/admin/tags/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid TagFormRequest request
    ) {
        tagService.update(id, request.name());
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "태그가 수정되었습니다.")
                        .build())
                .header("HX-Location", "/admin/tags")
                .build();
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        var tag = tagQueryService.getTagById(id);
        model.addAttribute("tag", tag);
        return "pages/admin/tags/remove";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "태그가 삭제되었습니다.")
                        .build())
                .header("HX-Location", "/admin/tags")
                .build();
    }

    public record TagFormRequest(
            @NotBlank
            @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "영문, 숫자, 하이픈(-)만 입력 가능합니다")
            String name
    ) {
    }
}
