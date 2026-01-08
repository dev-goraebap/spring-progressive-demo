package xyz.goraebap.blog.app.admin.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.goraebap.blog.app.admin.service.CuratedSourceService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurationScheduler {

    private final CuratedSourceService curatedSourceService;

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
        } catch (Exception e) {
            log.error("[Curation Scheduler] Failed to fetch sources", e);
        }
    }
}
