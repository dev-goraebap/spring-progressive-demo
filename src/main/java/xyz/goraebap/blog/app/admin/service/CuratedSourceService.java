package xyz.goraebap.blog.app.admin.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.*;
import xyz.goraebap.blog.app.admin.dto.AdminCuratedSourceFormRequest;
import xyz.goraebap.blog.shared.exception.BadRequestException;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CuratedSourceService {

    private final CuratedSourceRepository sourceRepository;
    private final CuratedItemRepository itemRepository;

    public CuratedSourceEntity create(AdminCuratedSourceFormRequest req) {
        // URL 중복 검증
        if (sourceRepository.findByUrl(req.getUrl()).isPresent()) {
            throw new BadRequestException("이미 등록된 RSS URL입니다.");
        }

        var source = CuratedSourceEntity.create(req);
        return sourceRepository.save(source);
    }

    public void update(Long sourceId, AdminCuratedSourceFormRequest req) {
        var source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundException("소스를 찾을 수 없습니다."));

        // URL 변경 시 중복 검증
        if (!source.getUrl().equals(req.getUrl())) {
            if (sourceRepository.findByUrl(req.getUrl()).isPresent()) {
                throw new BadRequestException("이미 등록된 RSS URL입니다.");
            }
        }

        source.update(req);
        sourceRepository.save(source);
    }

    public void toggleActive(Long sourceId) {
        var source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundException("소스를 찾을 수 없습니다."));
        source.toggleActive();
        sourceRepository.save(source);
    }

    public void delete(Long sourceId) {
        var source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundException("소스를 찾을 수 없습니다."));

        // 관련 항목 삭제
        itemRepository.deleteBySourceId(sourceId);

        sourceRepository.delete(source);
    }

    /**
     * 특정 소스에서 RSS 피드 가져오기
     */
    public int fetchFromSource(Long sourceId) {
        var source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundException("소스를 찾을 수 없습니다."));

        return fetchRssFeed(source);
    }

    /**
     * 모든 활성 소스에서 RSS 피드 가져오기
     */
    public FetchResult fetchAllActiveSources() {
        var activeSources = sourceRepository.findByIsActiveYn("Y");
        var results = new ArrayList<SourceFetchResult>();
        int total = 0;

        for (var source : activeSources) {
            try {
                int count = fetchRssFeed(source);
                results.add(new SourceFetchResult(source.getName(), count));
                total += count;
            } catch (Exception e) {
                log.error("[RSS Fetch] Failed to fetch from source: {} - {}", source.getName(), e.getMessage());
                results.add(new SourceFetchResult(source.getName(), -1)); // -1 = 실패
            }
        }

        return new FetchResult(total, results);
    }

    private int fetchRssFeed(CuratedSourceEntity source) {
        int newItemCount = 0;

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(source.getUrl())));

            List<SyndEntry> entries = feed.getEntries();
            int limit = Math.min(entries.size(), 10); // 최근 10개만 처리

            for (int i = 0; i < limit; i++) {
                SyndEntry entry = entries.get(i);

                String guid = entry.getUri() != null ? entry.getUri() : entry.getLink();
                String link = entry.getLink();

                // 중복 체크
                if (itemRepository.findByGuid(guid).isPresent() ||
                    itemRepository.findByLink(link).isPresent()) {
                    continue;
                }

                // snippet 추출
                String snippet = null;
                if (entry.getDescription() != null) {
                    snippet = entry.getDescription().getValue();
                    // HTML 태그 제거
                    snippet = snippet.replaceAll("<[^>]*>", "").trim();
                }

                // 발행일
                LocalDateTime pubDate = null;
                if (entry.getPublishedDate() != null) {
                    pubDate = entry.getPublishedDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                }

                var item = CuratedItemEntity.create(
                        entry.getTitle(),
                        link,
                        guid,
                        snippet,
                        pubDate,
                        source.getName(),
                        source.getId()
                );

                itemRepository.save(item);
                newItemCount++;
            }

            log.info("[RSS Fetch] Fetched {} new items from: {}", newItemCount, source.getName());

        } catch (Exception e) {
            log.error("[RSS Fetch] Error fetching from {}: {}", source.getName(), e.getMessage());
            throw new BadRequestException("RSS 피드를 가져오는데 실패했습니다: " + e.getMessage());
        }

        return newItemCount;
    }

    /**
     * 단일 항목 삭제
     */
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    /**
     * 오래된 항목 삭제
     */
    public int cleanupOldItems(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        int deleted = itemRepository.deleteOldItems(threshold);
        log.info("[RSS Cleanup] Deleted {} items older than {} days", deleted, days);
        return deleted;
    }

    public record FetchResult(int total, List<SourceFetchResult> sources) {}
    public record SourceFetchResult(String name, int count) {}
}
