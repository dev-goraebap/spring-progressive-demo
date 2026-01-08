package xyz.goraebap.blog.app.admin.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.goraebap.blog.app.admin.domain.FcmSubscriptionRepository;
import xyz.goraebap.blog.app.admin.service.CuratedSourceService;
import xyz.goraebap.blog.shared.firebase.FirebaseService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurationScheduler {

    private final CuratedSourceService curatedSourceService;
    private final FirebaseService firebaseService;
    private final FcmSubscriptionRepository fcmSubscriptionRepository;

    /**
     * 큐레이션 소스 자동 fetch
     * 매일 09:00, 12:00, 18:00, 21:00 실행
     */
    @Scheduled(cron = "0 0 9,12,18,21 * * *")
    public void fetchAllSources() {
        log.info("[Curation Scheduler] Starting scheduled fetch");
        try {
            var result = curatedSourceService.fetchAllActiveSources();
            log.info("[Curation Scheduler] Completed. Total {} items fetched", result.total());

            // 새 항목이 있으면 FCM 발송
            if (result.total() > 0) {
                sendPushNotification(result.total());
            }
        } catch (Exception e) {
            log.error("[Curation Scheduler] Failed to fetch sources", e);
        }
    }

    private void sendPushNotification(int count) {
        var tokens = fcmSubscriptionRepository.findAllTokens();
        if (tokens.isEmpty()) return;

        int sent = firebaseService.sendToAll(
                tokens,
                "새로운 개발 소식",
                count + "개의 새로운 큐레이션이 도착했습니다.",
                "/curations"
        );
        log.info("[Curation Scheduler] FCM 발송 완료 - {}명에게 전송", sent);
    }
}
