package xyz.goraebap.spring_progressive_demo.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.SeriesViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminSeriesViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostSeriesNavViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesDetailViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel;

import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesQueryService {

    private final SeriesViewMapper seriesViewMapper;
    private final ThumbnailEnricher thumbnailEnricher;

    public List<SeriesViewModel> getAllSeries() {
        var seriesList = seriesViewMapper.findAllSeries();
        seriesList.forEach(thumbnailEnricher::enrich);
        return seriesList;
    }

    public SeriesDetailViewModel getSeriesWithPosts(String slug) {
        var series = seriesViewMapper.findSeriesWithPosts(slug);
        if (series == null) {
            return null;
        }

        thumbnailEnricher.enrich(series);
        series.getPosts().forEach(thumbnailEnricher::enrich);

        return series;
    }

    public PostSeriesNavViewModel getSeriesNavByPostId(Long postId) {
        var nav = seriesViewMapper.findSeriesNavByPostId(postId);
        if (nav != null) {
            thumbnailEnricher.enrich(nav);
        }
        return nav;
    }

    public Pagination<AdminSeriesViewModel> getAdminSeriesWithPagination(
            String name,
            String status,
            String isPublishedYn,
            int page,
            int size
    ) {
        int offset = Pagination.getOffset(page, size);
        var items = seriesViewMapper.findAdminSeries(name, status, isPublishedYn, size, offset);
        items.forEach(thumbnailEnricher::enrich);
        int totalCount = seriesViewMapper.countAdminSeries(name, status, isPublishedYn);
        return Pagination.of(items, page, totalCount, null, size);
    }

    public AdminSeriesViewModel getSeriesById(Long id) {
        var series = seriesViewMapper.findSeriesById(id);
        if (series == null) {
            throw new NotFoundException("시리즈를 찾을 수 없습니다.");
        }
        thumbnailEnricher.enrich(series);
        return series;
    }
}
