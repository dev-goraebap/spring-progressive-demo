package xyz.goraebap.blog.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.AttachmentEntity;
import xyz.goraebap.blog.app.admin.domain.AttachmentRepository;
import xyz.goraebap.blog.app.admin.domain.SeriesEntity;
import xyz.goraebap.blog.app.admin.domain.SeriesRepository;
import xyz.goraebap.blog.app.admin.dto.AdminSeriesFormRequest;
import xyz.goraebap.blog.shared.config.CacheConfig;
import xyz.goraebap.blog.shared.exception.BadRequestException;
import xyz.goraebap.blog.shared.exception.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final AttachmentRepository attachmentRepository;

    @CacheEvict(value = CacheConfig.SERIES, allEntries = true)
    public SeriesEntity create(AdminSeriesFormRequest req, Long userId) {
        // 이름 중복 검증
        if (seriesRepository.findByName(req.getName()).isPresent()) {
            throw new BadRequestException("이미 사용 중인 시리즈 이름입니다.");
        }

        // slug 중복 검증
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            if (seriesRepository.findBySlug(req.getSlug()).isPresent()) {
                throw new BadRequestException("이미 사용 중인 슬러그입니다.");
            }
        }

        var series = SeriesEntity.create(req, userId);
        seriesRepository.save(series);

        // 썸네일 첨부
        if (req.getThumbnailBlobId() != null) {
            attachThumbnail(series.getId(), req.getThumbnailBlobId());
        }

        return series;
    }

    @Caching(evict = {
            @CacheEvict(value = CacheConfig.SERIES, allEntries = true),
            @CacheEvict(value = CacheConfig.SERIES_DETAIL, allEntries = true)
    })
    public void update(Long seriesId, AdminSeriesFormRequest req) {
        var series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException("시리즈를 찾을 수 없습니다."));

        // 이름 변경 시 중복 검증
        if (!series.getName().equals(req.getName())) {
            if (seriesRepository.findByName(req.getName()).isPresent()) {
                throw new BadRequestException("이미 사용 중인 시리즈 이름입니다.");
            }
        }

        // slug 변경 시 중복 검증
        if (req.getSlug() != null && !req.getSlug().isBlank() && !req.getSlug().equals(series.getSlug())) {
            if (seriesRepository.findBySlug(req.getSlug()).isPresent()) {
                throw new BadRequestException("이미 사용 중인 슬러그입니다.");
            }
        }

        series.update(req);
        seriesRepository.save(series);

        // 썸네일 업데이트
        if (req.getThumbnailBlobId() != null) {
            attachThumbnail(seriesId, req.getThumbnailBlobId());
        }
    }

    @Caching(evict = {
            @CacheEvict(value = CacheConfig.SERIES, allEntries = true),
            @CacheEvict(value = CacheConfig.SERIES_DETAIL, allEntries = true),
            @CacheEvict(value = CacheConfig.SERIES_NAV, allEntries = true)
    })
    public void delete(Long seriesId) {
        var series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException("시리즈를 찾을 수 없습니다."));

        // 썸네일 첨부 삭제
        attachmentRepository.deleteByRecordTypeAndRecordId("series", seriesId.toString());

        seriesRepository.delete(series);
    }

    private void attachThumbnail(Long seriesId, Long blobId) {
        String recordType = "series";
        String recordId = seriesId.toString();
        String name = "thumbnail";

        // 기존 썸네일 삭제
        attachmentRepository.deleteByRecordTypeAndRecordIdAndName(recordType, recordId, name);

        // 새 썸네일 첨부
        var attachment = AttachmentEntity.create(name, recordType, recordId, blobId);
        attachmentRepository.save(attachment);
    }
}
