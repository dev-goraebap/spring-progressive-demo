package xyz.goraebap.blog.app.admin.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.blog.app.admin.domain.FcmSubscriptionRepository;
import xyz.goraebap.blog.app.admin.dto.AdminFcmIndexRequest;
import xyz.goraebap.blog.app.admin.dto.FcmSendRequest;
import xyz.goraebap.blog.infra.service.FcmSubscriptionQueryService;
import xyz.goraebap.blog.shared.firebase.FirebaseService;
import xyz.goraebap.blog.shared.htmx.HxTrigger;

import java.util.List;

@Controller
@RequestMapping("/admin/fcm")
@RequiredArgsConstructor
public class AdminFcmController {

    private final FcmSubscriptionQueryService fcmSubscriptionQueryService;
    private final FcmSubscriptionRepository fcmSubscriptionRepository;
    private final FirebaseService firebaseService;

    /**
     * FCM 구독자 목록 + 통계
     */
    @GetMapping
    public String index(@ModelAttribute("req") AdminFcmIndexRequest req, Model model) {
        var subscriptionData = fcmSubscriptionQueryService.getSubscriptionsWithPagination(
                req.getSearch(),
                req.getPage(),
                req.getSize()
        );
        model.addAttribute("subscriptionData", subscriptionData);
        model.addAttribute("totalCount", fcmSubscriptionQueryService.getTotalCount());
        model.addAttribute("browserStats", fcmSubscriptionQueryService.getStatsByBrowser());
        model.addAttribute("osStats", fcmSubscriptionQueryService.getStatsByOs());
        model.addAttribute("deviceStats", fcmSubscriptionQueryService.getStatsByDeviceType());
        return "pages/admin/fcm/index";
    }

    /**
     * 발송 폼 (모달)
     */
    @GetMapping("/send")
    public String send(Model model) {
        model.addAttribute("totalCount", fcmSubscriptionQueryService.getTotalCount());
        return "pages/admin/fcm/send";
    }

    /**
     * 전체 발송
     */
    @PostMapping("/send")
    public ResponseEntity<Void> sendToAll(@Valid @ModelAttribute FcmSendRequest req) {
        List<String> tokens = fcmSubscriptionRepository.findAllTokens();

        if (tokens.isEmpty()) {
            return ResponseEntity.ok()
                    .header("HX-Trigger", HxTrigger.builder()
                            .closeModal()
                            .toast("warning", "발송할 구독자가 없습니다.")
                            .build())
                    .build();
        }

        int successCount = firebaseService.sendToAll(tokens, req.getTitle(), req.getBody(), req.getUrl());

        String message = String.format("%d명에게 발송 완료 (성공: %d)", tokens.size(), successCount);

        return ResponseEntity.ok()
                .header("HX-Trigger", HxTrigger.builder()
                        .closeModal()
                        .toast("success", message)
                        .build())
                .header("HX-Location", "/admin/fcm")
                .build();
    }
}
