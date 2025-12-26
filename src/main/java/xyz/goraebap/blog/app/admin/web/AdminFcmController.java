package xyz.goraebap.blog.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.goraebap.blog.app.admin.dto.AdminFcmIndexRequest;
import xyz.goraebap.blog.infra.service.FcmSubscriptionQueryService;

@Controller
@RequestMapping("/admin/fcm")
@RequiredArgsConstructor
public class AdminFcmController {

    private final FcmSubscriptionQueryService fcmSubscriptionQueryService;

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
}
