package xyz.goraebap.spring_progressive_demo.app.admin.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.SeriesPostOrderRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.SeriesPostService;
import xyz.goraebap.spring_progressive_demo.infra.service.SeriesQueryService;
import xyz.goraebap.spring_progressive_demo.shared.htmx.HxTrigger;

@Controller
@RequestMapping("/admin/series/{seriesId}/posts")
@RequiredArgsConstructor
public class AdminSeriesPostController {

    private final SeriesQueryService seriesQueryService;
    private final SeriesPostService seriesPostService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String index(@PathVariable Long seriesId, Model model) {
        var series = seriesQueryService.getSeriesById(seriesId);
        var posts = seriesQueryService.getSeriesPostsById(seriesId);
        model.addAttribute("series", series);
        model.addAttribute("posts", posts);
        return "pages/admin/series/posts/index";
    }

    @GetMapping("/add")
    public String add(
            @PathVariable Long seriesId,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        var posts = seriesQueryService.getPostsNotInSeries(seriesId, keyword);
        model.addAttribute("seriesId", seriesId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", posts);
        return "pages/admin/series/posts/add";
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable Long seriesId,
            @RequestParam Long postId
    ) {
        seriesPostService.addPost(seriesId, postId);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", "포스트가 추가되었습니다.")
                        .build())
                .header("HX-Location", "/admin/series/" + seriesId + "/posts")
                .build();
    }

    @PutMapping("/orders")
    public ResponseEntity<Void> updateOrders(
            @PathVariable Long seriesId,
            @RequestParam String orders
    ) throws JsonProcessingException {
        var req = objectMapper.readValue(orders, SeriesPostOrderRequest.class);
        var items = req.getItems().stream()
                .map(i -> new SeriesPostService.OrderItem(i.getId(), i.getSortOrder()))
                .toList();
        seriesPostService.updateOrders(items);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "순서가 저장되었습니다.")
                        .build())
                .build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> destroy(
            @PathVariable Long seriesId,
            @PathVariable Long postId
    ) {
        seriesPostService.removePost(seriesId, postId);
        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .toast("success", "포스트가 제거되었습니다.")
                        .build())
                .header("HX-Location", "/admin/series/" + seriesId + "/posts")
                .build();
    }
}
