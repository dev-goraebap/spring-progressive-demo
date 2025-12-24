package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminBlockedIpIndexRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.dto.BlockedIpFormRequest;
import xyz.goraebap.spring_progressive_demo.app.admin.service.BlockedIpService;
import xyz.goraebap.spring_progressive_demo.infra.service.BlockedIpQueryService;

@Controller
@RequestMapping("/admin/blocked-ips")
@RequiredArgsConstructor
public class AdminBlockedIpController {

    private final BlockedIpService blockedIpService;
    private final BlockedIpQueryService blockedIpQueryService;

    /**
     * 목록 페이지
     */
    @GetMapping
    public String index(@ModelAttribute("req") AdminBlockedIpIndexRequest req, Model model) {
        var blockedIpData = blockedIpQueryService.getBlockedIpsWithPagination(
                req.getSearch(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("blockedIpData", blockedIpData);
        return "pages/admin/blocked-ips/index";
    }

    /**
     * 등록 폼 (모달)
     */
    @GetMapping("/add")
    public String add() {
        return "pages/admin/blocked-ips/add";
    }

    /**
     * IP 차단 등록
     */
    @PostMapping
    public ResponseEntity<Void> store(@ModelAttribute BlockedIpFormRequest req) {
        blockedIpService.block(req);
        return ResponseEntity.ok()
                .header("HX-Trigger", "blockedIpChanged")
                .header("HX-Trigger-After-Swap", "modalClose")
                .build();
    }

    /**
     * 삭제 확인 모달
     */
    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Model model) {
        var blockedIp = blockedIpQueryService.getById(id);
        model.addAttribute("blockedIp", blockedIp);
        return "pages/admin/blocked-ips/remove";
    }

    /**
     * 차단 해제
     */
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable Long id) {
        blockedIpService.unblock(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", "blockedIpChanged")
                .build();
    }

    /**
     * 영구 차단으로 변경
     */
    @PatchMapping("/{id}/permanent")
    public ResponseEntity<Void> permanent(@PathVariable Long id) {
        blockedIpService.makePermanent(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", "blockedIpChanged")
                .build();
    }

    /**
     * 레코드 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        blockedIpService.delete(id);
        return ResponseEntity.ok()
                .header("HX-Trigger", "blockedIpChanged")
                .header("HX-Trigger-After-Swap", "modalClose")
                .build();
    }
}
